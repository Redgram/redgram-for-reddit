package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.matie.redgram.ui.App;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
        postItemTextView.setupView(item);

        thumbnailView.setHierarchy(getDraweeHierarchy(item));
        thumbnailView.setController(getDraweeController(item));

//        add transparency
        overlay.setBackgroundColor(OVERLAY_OPACITY * 0x1000000);
        overlayText.setText(item.getType().toString());

    }

    private GenericDraweeHierarchy getDraweeHierarchy(PostItem item) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources())
                        .setFadeDuration(300)
                        .setActualImageFocusPoint(new PointF(0.5f, 0f));

//        if(item.getType() == PostItem.Type.GIF)
//            builder.setOverlay(ContextCompat.getDrawable(getContext(), R.drawable.image_nsfw_overlay));
//        else  if(item.getType() == PostItem.Type.YOUTUBE)
//            builder.setOverlay(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_keyboard_arrow_up));
//        else if(item.getType() == PostItem.Type.GFYCAT)
//            builder.setOverlay(ContextCompat.getDrawable(getContext(), R.drawable.ic_action_favorite));
//


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

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }
}
