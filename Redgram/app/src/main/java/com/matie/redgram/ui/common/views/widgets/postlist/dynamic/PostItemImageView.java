package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemImageView extends PostBaseView {

    @InjectView(R.id.image_view)
    SimpleDraweeView imageView;

    @InjectView(R.id.image_overlay)
    RelativeLayout imageOverlay;

    @InjectView(R.id.image_text_view)
    PostItemTextView postItemTextView;

    public PostItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setUpView(PostItem item) {

        postItemTextView.setUpView(item);

        if(item.isAdult()){
            imageOverlay.setVisibility(VISIBLE);
        }else{
            imageOverlay.setVisibility(INVISIBLE);
        }

        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest thumbnail = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();

        Uri uri = Uri.parse(item.getUrl());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .build();

        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(thumbnail)
                .setImageRequest(request)
                .setOldController(imageView.getController());

        if(item.getType() == PostItem.Type.GIF)
            builder.setAutoPlayAnimations(true);

        DraweeController controller = builder.build();
        imageView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
        imageView.setController(controller);
    }

    @OnClick(R.id.image_overlay)
    public void onClick(){
        if(imageOverlay.getVisibility() == VISIBLE){
            imageOverlay.setVisibility(INVISIBLE);
            // TODO: 21/09/15 add animation, ex: fade in/out
        }
    }
}
