package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Closeables;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * todo: replace error image to reddit "not found" icon :)
 * Created by matie on 04/04/15.
 */
public class PostItemImageView extends PostItemSubView{


    @InjectView(R.id.image_view)
    SimpleDraweeView imageView;

    @InjectView(R.id.image_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.image_overlay)
    RelativeLayout imageOverlay;

    PostItem postItem;
    Uri imageUri;
    boolean imageLoaded;

    private ControllerListener<? super ImageInfo> controllerListener;

    public PostItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item) {
        imageLoaded = false;
        postItem = item;

        postItemTextView.setupView(item);

        imageView.setHierarchy(getDraweeHierarchy(item));
        imageView.setController(getDraweeController(item));


        if(item.isAdult() && !isNsfwDisabled()){
            imageOverlay.setVisibility(VISIBLE);
        }else{
            imageOverlay.setVisibility(GONE);
        }

    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {
        if(disabled){
            imageOverlay.setVisibility(GONE);
        }else{
            imageOverlay.setVisibility(VISIBLE);
        }
    }

    private GenericDraweeHierarchy getDraweeHierarchy(PostItem item) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setActualImageFocusPoint(new PointF(0.5f, 0f))
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        return hierarchy;
    }

    private DraweeController getDraweeController(PostItem item) {
//        Uri thumbnailUri = Uri.parse(item.getThumbnail());
//        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
//                .build();

        // TODO: 2015-11-06 load based on user preference
        String itemUrl = item.getUrl();
        itemUrl = itemUrl.substring(0,itemUrl.lastIndexOf('.')) + "l" + itemUrl.substring(itemUrl.lastIndexOf('.'));

        Uri uri = Uri.parse(itemUrl);
        imageUri = uri;

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
//                .setLowResImageRequest(thumbnail)
                .setImageRequest(request)
                .setOldController(imageView.getController())
                .setControllerListener(getControllerListener());

        DraweeController controller = builder.build();
        return controller;
    }


    private ControllerListener<? super ImageInfo> getControllerListener() {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                imageLoaded = true;
//                getMainActivity().getApp().getToastHandler().showBackgroundToast(imageInfo.getQualityInfo()+"", Toast.LENGTH_LONG);
            }

        };
        return controllerListener;
    }

    @OnClick(R.id.image_overlay)
    public void onOverlayClick(){
        handleOverlayClickEvent();
    }

    @OnClick(R.id.image_view)
    public void onImageClick(){

        if(imageLoaded){

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(imageUri));

            if(cacheKey != null){
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);

                File localFile;
                if(resource != null){
                    localFile = ((FileBinaryResource) resource).getFile();

                    Bundle bundle = new Bundle();

                    bundle.putString(ImagePreviewFragment.LOCAL_CACHE_KEY, localFile.getPath());

                    bundle.putString(ImagePreviewFragment.MAIN_DATA, new Gson().toJson(postItem));

                    getMainActivity().setPanelView(Fragments.IMAGE_PREVIEW, bundle);
                }

            }

        }
    }

    private void handleOverlayClickEvent(){
        if(imageOverlay.getVisibility() == GONE){
            if(getContext() instanceof MainActivity){
                ((MainActivity) getContext()).getApp().getToastHandler().showToast("NSFW Disabled", Toast.LENGTH_LONG);
            }
        }else if(imageOverlay.getVisibility() == VISIBLE){
            if(!isNsfwDisabled()){
                callNsfwDialog();
            }
        }
    }

}
