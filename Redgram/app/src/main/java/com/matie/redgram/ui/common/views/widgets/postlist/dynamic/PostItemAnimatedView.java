package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.previews.WebPreviewFragment;
import com.matie.redgram.ui.common.utils.ToastHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 19/05/15.
 */
public class PostItemAnimatedView extends PostItemSubView {

    public static final int OVERLAY_OPACITY = 120;

    @InjectView(R.id.gif_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.image_view)
    SimpleDraweeView thumbnailView;

//    include tag
    @InjectView(R.id.animated_overlay)
    FrameLayout animatedOverlay;

    @InjectView(R.id.overlay_image)
    ImageView overlayImage;

    @InjectView(R.id.overlay_text)
    TextView overlayText;

    @InjectView(R.id.overlay)
    View overlay;

    PostItem postItem;

    public PostItemAnimatedView(Context context, AttributeSet attrs) {
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

        thumbnailView.setHierarchy(getDraweeHierarchy(item));
        thumbnailView.setController(getDraweeController(item));

//        add transparency
        overlay.setBackgroundColor(OVERLAY_OPACITY * 0x1000000);
        overlayText.setText(item.getType().toString());
        overlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_play_circle_fill));
    }

    private GenericDraweeHierarchy getDraweeHierarchy(PostItem item) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources())
                        .setFadeDuration(300)
                        .setActualImageFocusPoint(new PointF(0.5f, 0f));
        return builder.build();
    }

    private DraweeController getDraweeController(PostItem item) {
        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(thumbnail)
                .setOldController(thumbnailView.getController());

        DraweeController controller = builder.build();
        return controller;
    }

    @OnClick(R.id.overlay_image)
    public void onClick(){
        handlePlayClickEvent();
    }

    private void handlePlayClickEvent() {
        if(animatedOverlay.getVisibility() == GONE){
            // TODO: 2015-10-20 handle media
            if(getContext() instanceof MainActivity){
                ((MainActivity) getContext()).getApp().getToastHandler().showToast("NSFW diabled", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {
        if(disabled){
            animatedOverlay.setVisibility(GONE);
        }else{
            animatedOverlay.setVisibility(VISIBLE);
        }
    }

    @OnClick({R.id.animated_overlay, R.id.overlay_image, R.id.overlay_text})
    public void onGalleryClick(){
        if(postItem != null){
            Bundle bundle = new Bundle();
            bundle.putString(WebPreviewFragment.MAIN_DATA, new Gson().toJson(postItem));
            getMainActivity().setPanelView(Fragments.WEB_PREVIEW, bundle);
        }
    }

}
