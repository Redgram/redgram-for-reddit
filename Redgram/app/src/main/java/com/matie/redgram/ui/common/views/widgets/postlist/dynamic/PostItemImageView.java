package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.media.images.ImageManager;
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
    String imageUrl;
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

        ImageManager imageManager =  getMainActivity().getImageManager();

        imageUrl = item.getUrl();
//        imageUrl = imageUrl.substring(0,imageUrl.lastIndexOf('.')) + "l" + imageUrl.substring(imageUrl.lastIndexOf('.'));

        imageManager.newImageBuilder(getContext())
                .setImageView(imageView)
                .setImage(imageUrl, true)
                .includeOldController()
                .setListener(getControllerListener())
                .build();

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

    private ControllerListener<? super ImageInfo> getControllerListener() {
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                imageLoaded = true;
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

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(imageUrl)));

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
