package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.previews.ImagePreviewFragment;

import java.io.File;

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
                .build();
        return hierarchy;
    }

    private DraweeController getDraweeController(PostItem item) {
        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();

        Uri uri = Uri.parse(item.getUrl());
        imageUri = uri;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(thumbnail)
                .setImageRequest(request)
                .setOldController(imageView.getController());

        DraweeController controller = builder.build();
        return controller;
    }

    @OnClick(R.id.image_overlay)
    public void onOverlayClick(){
        handleOverlayClickEvent();
    }

    @OnClick(R.id.image_view)
    public void onImageClick(){

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





//    private class DiskExecutor implements Executor{
//        @Override
//        public void execute(Runnable command) {
//            command.run();
//        }
//    }


//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        DataSource<Boolean> inDiskCacheSource = imagePipeline.isInDiskCache(imageUri);
//        DataSubscriber<Boolean> subscriber = new BaseDataSubscriber<Boolean>() {
//            @Override
//            protected void onNewResultImpl(DataSource<Boolean> dataSource) {
//                if (!dataSource.isFinished()) {
//                    return;
//                }
//                boolean isInCache = dataSource.getResult();
//                MainActivity mainActivity = getMainActivity();
//
//                mainActivity.getApp().getToastHandler().showBackgroundToast((isInCache)?"cached":"not cached", Toast.LENGTH_LONG);
//
//                // your code here
//                if(isInCache){
//                    CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(imageUri));
//                    BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
//
//                    File localFile = ((FileBinaryResource) resource).getFile();
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(ImagePreviewFragment.LOCAL_CACHE_KEY, localFile.getPath());
//
//                    mainActivity.setPanelView(Fragments.IMAGE_PREVIEW, bundle);
//                }
//            }
//
//            @Override
//            protected void onFailureImpl(DataSource<Boolean> dataSource) {
//
//            }
//        };
//
//        inDiskCacheSource.subscribe(subscriber, new DiskExecutor());
}
