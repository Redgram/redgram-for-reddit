package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 19/05/15.
 * todo: FIND OUT WHY THUMBNAILS POSITION IS NOT STABLE
 */
public class PostItemDefaultView extends PostBaseView {

    @InjectView(R.id.default_thumbnail_view)
    SimpleDraweeView thumbnailView;

    @InjectView(R.id.default_text_view)
    PostItemTextView postItemTextView;

    @InjectView(R.id.default_tag_view)
    PostItemTagView postItemTagView;

    public PostItemDefaultView(Context context, AttributeSet attrs) {
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
        postItemTagView.setUpView(item);

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
}
