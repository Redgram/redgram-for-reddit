package com.matie.redgram.ui.home.views.widgets.postlist;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemDefaultView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemGalleryView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemGifView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemImageView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemImgurView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemSelfView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemView extends CardView {

    @InjectView(R.id.post_wrapper)
    RelativeLayout postWrapper;

    @InjectView(R.id.post_item_header_view)
    PostItemHeaderView postItemHeaderView;
    @InjectView(R.id.header_title_view)
    TextView headerTitleView;
    @InjectView(R.id.header_time_view)
    TextView headerTimeView;

    @InjectView(R.id.post_item_text_view)
    PostItemTextView postItemTextView;
    @InjectView(R.id.text_title_view)
    TextView textTitleView;
    @InjectView(R.id.text_content_view)
    TextView textContentView;

    @InjectView(R.id.post_item_action_view)
    PostItemActionView postItemActionView;
    @InjectView(R.id.comments_action_button)
    TextView commentsActionButton;
    @InjectView(R.id.reply_action_button)
    TextView replyActionButton;
    @InjectView(R.id.source_action_view)
    TextView sourceActionView;

    @InjectView(R.id.post_item_dynamic_view)
    ViewGroup dynamicParent;

    //find view according to type and bind items to it - do not use ButterKnife annotation here
    View dynamicView;

    final Resources res;
    final Context context;

    Uri uri;

    public PostItemView(Context context) {
        super(context);
        this.context = context;
        res = context.getResources();
    }

    public PostItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        res = context.getResources();
    }

    public PostItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        res = context.getResources();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public ViewGroup getDynamicParent() {
        return dynamicParent;
    }

    public void setDynamicParent(ViewGroup dynamicParent) {
        this.dynamicParent = dynamicParent;
    }

    public View getDynamicView() {
        return dynamicView;
    }

    public void setDynamicView(View dynamicView) {
        this.dynamicView = dynamicView;
    }

    public void bindTo(PostItem item, int position){
        requestLayout();

        if(position == 0){
            resolveFirstItemDimensions();
        }else{
            resolveOtherItemsDimensions();
        }

        headerTitleView.setText(item.getAuthor());
        headerTimeView.setText(item.getTime() + "");

        //self view should have their own title and body with a thumbnail
        if(!(dynamicView instanceof PostItemSelfView))
            setUpTextBody(item);

        getAndSetUpView(item);

        commentsActionButton.setText(item.getNumComments()+ " comments");
        sourceActionView.setText("("+item.getDomain()+")");
    }

    private void setUpTextBody(PostItem item) {
        textTitleView.setText(item.getTitle());
        if(item.getText().length() > 0){
            textContentView.setText(item.getText());
            textContentView.setVisibility(VISIBLE);
        }
    }

    private void getAndSetUpView(PostItem item) {
        if(dynamicView instanceof PostItemImageView){
            setUpImage(item);
            return;
        }
        if(dynamicView instanceof PostItemGifView){
            //todo
            //dummy
            TextView textView = new TextView(context);
            textView.setText(item.getType().toString());
            ((PostItemGifView) dynamicView).removeAllViews();
            ((PostItemGifView) dynamicView).addView(textView);
            return;
        }
        if(dynamicView instanceof PostItemImgurView){
            //todo
            //dummy
            TextView textView = new TextView(context);
            textView.setText(item.getType().toString());
            ((PostItemImgurView) dynamicView).removeAllViews();
            ((PostItemImgurView) dynamicView).addView(textView);
            return;
        }
        if(dynamicView instanceof PostItemGalleryView){
            //todo
            //dummy
            TextView textView = new TextView(context);
            textView.setText(item.getType().toString());
            ((PostItemGalleryView) dynamicView).removeAllViews();
            ((PostItemGalleryView) dynamicView).addView(textView);
            return;
        }
        if(dynamicView instanceof PostItemSelfView){
            //todo
            //dummy
            TextView textView = new TextView(context);
            textView.setText(item.getType().toString());
            ((PostItemSelfView) dynamicView).removeAllViews();
            ((PostItemSelfView) dynamicView).addView(textView);

            textTitleView.setVisibility(GONE);
            textContentView.setVisibility(GONE);
            return;
        }
        if(dynamicView instanceof PostItemDefaultView){
            //todo
            //dummy
            TextView textView = new TextView(context);
            textView.setText(item.getType().toString());
            ((PostItemDefaultView) dynamicView).removeAllViews();
            ((PostItemDefaultView) dynamicView).addView(textView);
            return;
        }
    }

    private void setUpImage(PostItem item) {

        SimpleDraweeView imageView = (SimpleDraweeView)((PostItemImageView)dynamicView).findViewById(R.id.image_view);

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

    /**
     * HACK: This will only be applied to the first item. It will push the item downwards equal to the toolbar height.
     * This is because the view and toolbar are on top of each other. Otherwise, hiding the toolbar will leave a gap.
     * Since the height of the actionbar is not hardcoded, we get the height from the application attributes,
     * and reset the layout params of the {@link #PostItemView(android.content.Context)}.
     *
     * This class extends CardView which in fact is a RelativeView by default.
     */
    private void resolveFirstItemDimensions() {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, res.getDisplayMetrics());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.getLayoutParams());
            lp.setMargins(20, actionBarHeight+50, 20, 50);
            this.setLayoutParams(lp);
        }
    }
    /**
     * other items
     */
    private void resolveOtherItemsDimensions() {
        //since the recycler view reuses the same items, make sure every other item is set to the intended margins
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.getLayoutParams());
        lp.setMargins(20, 0, 20, 50);
        this.setLayoutParams(lp);
    }

}
