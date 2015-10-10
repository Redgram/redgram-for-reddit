package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
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
 * todo: FIND OUT WHY THUMBNAILS POSITION IS NOT STABLE
 */
public class PostItemDefaultView extends PostItemSubView {

    @InjectView(R.id.default_thumbnail_view)
    SimpleDraweeView thumbnailView;

    @InjectView(R.id.default_source_text)
    TextView postSourceText;

    @InjectView(R.id.default_link_text)
    TextView postLinkText;

    @InjectView(R.id.default_text_view)
    PostItemTextView postItemTextView;

    public PostItemDefaultView(Context context, AttributeSet attrs) {
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

        if(item.getType() == PostItem.Type.IMGUR){
            postLinkText.setTextColor(getResources().getColor(R.color.material_green400));
            postSourceText.setTextColor(getResources().getColor(R.color.material_green400));
        }else{
            postLinkText.setTextColor(getResources().getColor(R.color.material_grey500));
            postSourceText.setTextColor(getResources().getColor(R.color.material_grey500));
        }

        postSourceText.setText(item.getDomain());
        postLinkText.setText(item.getUrl());

        Uri thumbnailUri = Uri.parse(item.getThumbnail());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(thumbnailView.getController());

        DraweeController controller = builder.build();
        thumbnailView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
        thumbnailView.setController(controller);
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }
}
