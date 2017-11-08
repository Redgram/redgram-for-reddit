package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.matie.redgram.ui.links.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 19/05/15.
 */
public class PostItemGalleryView extends PostItemSubView {

    public static final int OVERLAY_OPACITY = 120;

    @InjectView(R.id.image_view)
    SimpleDraweeView thumbnailView;
    //    include tag
    @InjectView(R.id.gallery_overlay)
    FrameLayout galleryOverlay;

    @InjectView(R.id.gallery_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.overlay_image)
    ImageView overlayImage;

    @InjectView(R.id.overlay_text)
    TextView overlayText;

    @InjectView(R.id.overlay)
    View overlay;

    PostItem postItem;
    int position;
    LinksView listener;

    public PostItemGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, LinksView listener) {
        this.postItem = item;
        this.position = position;
        this.listener = listener;

        postItemTextView.setupView(item, position, listener);

        thumbnailView.setHierarchy(getDraweeHierarchy(item));
        thumbnailView.setController(getDraweeController(item));

//        add transparency
        overlay.setBackgroundColor(OVERLAY_OPACITY * 0x1000000);
        overlayText.setVisibility(GONE);
        overlayImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_photo_library));
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
        return builder.build();
    }


    @OnClick({R.id.gallery_overlay, R.id.overlay_image, R.id.overlay_text})
    public void onGalleryClick(){
        if(postItem.isAdult() && (!getSessionPrefs().isOver18() || getSessionPrefs().isDisableNsfwPreview())){
            listener.callAgeConfirmDialog();
        }else{
            listener.viewWebMedia(position);
        }
    }
}
