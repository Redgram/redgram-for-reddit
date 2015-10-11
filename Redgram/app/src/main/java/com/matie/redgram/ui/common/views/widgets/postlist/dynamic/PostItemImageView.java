package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;

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
//                .setOverlay(getResources().getDrawable(R.drawable.image_nsfw_overlay))
                .build();
        return hierarchy;
    }

    private DraweeController getDraweeController(PostItem item) {
        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();

        // TODO: 07/10/15 USE SHARED PREFERENCES TO STORE DEFAULT QUALITY, SEE IMGUR API
//        if(item.isImgurContent()){
//
//            int dot = item.getUrl().lastIndexOf('.');
//            String newUrl = item.getUrl().substring(0, dot) + "l" + item.getUrl().substring(dot,item.getUrl().length());
//            item.setUrl(newUrl);
//
//            Log.d("IMGUR URL", item.getUrl());
//        }

        Uri uri = Uri.parse(item.getUrl());
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
    public void onClick(){
        handleOverlayClickEvent();
    }

    private void handleOverlayClickEvent(){
        if(imageOverlay.getVisibility() == VISIBLE){
            if(!isNsfwDisabled()){
                callNsfwDialog();
            }
        }
    }

}
