package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 19/05/15.
 */
public class PostItemDefaultView extends PostItemSubView {

    @InjectView(R.id.default_wrapper)
    LinearLayout defaultWrapper;

    @InjectView(R.id.default_thumbnail_view)
    SimpleDraweeView thumbnailView;

    @InjectView(R.id.default_source_text)
    TextView postSourceText;

    @InjectView(R.id.default_link_text)
    TextView postLinkText;

    @InjectView(R.id.default_text_view)
    PostItemTextView postItemTextView;

    PostItem postItem;
    int position;
    LinksView listener;

    public PostItemDefaultView(Context context, AttributeSet attrs) {
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

        if(item.getType() == PostItem.Type.IMGUR){
            postLinkText.setTextColor(getResources().getColor(R.color.material_green400));
            postSourceText.setTextColor(getResources().getColor(R.color.material_green400));
        }else{
            postLinkText.setTextColor(getResources().getColor(R.color.material_grey600));
            postSourceText.setTextColor(getResources().getColor(R.color.material_grey600));
        }

        postSourceText.setText(item.getDomain());
        postLinkText.setText(item.getUrl());

        if(getUserPrefs().getMedia().equalsIgnoreCase("on") && !isNsfw()){
            setupThumbnail();
        }else if(getUserPrefs().getMedia().equalsIgnoreCase("subreddit")){
            if(!postItem.getThumbnail().isEmpty() && !isNsfw()){
                setupThumbnail();
            }else{
                thumbnailView.setVisibility(GONE);
            }
        }else{
            //no
            thumbnailView.setVisibility(GONE);
        }

    }

    private void setupThumbnail() {
        thumbnailView.setVisibility(VISIBLE);
        Uri thumbnailUri = Uri.parse(postItem.getThumbnail());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(thumbnailUri)
                .build();
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(thumbnailView.getController());

        DraweeController controller = builder.build();
        thumbnailView.getHierarchy().setActualImageFocusPoint(new PointF(0.5f, 0f));
        thumbnailView.setController(controller);
    }

    @OnClick(R.id.default_wrapper)
    public void onDefaultWrapperClick(){
        if(isNsfw()){
            listener.callAgeConfirmDialog();
        }else{
            listener.viewWebMedia(position);
        }
    }

    private boolean isNsfw(){
        if(postItem.isAdult() && !getUserPrefs().isOver18()){
            return true;
        }
        return false;
    }
}
