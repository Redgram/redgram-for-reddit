package com.matie.redgram.ui.home.views.widgets.postlist;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemDefaultView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemGalleryView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemGifView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemImageView;
import com.matie.redgram.ui.home.views.widgets.postlist.dynamic.PostItemTextView;

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

        getAndSetUpView(item);

        commentsActionButton.setText(item.getNumComments()+ " comments");
        sourceActionView.setText("("+item.getDomain()+")");
    }

    private void getAndSetUpView(PostItem item) {
        if(dynamicView instanceof PostItemDefaultView){
            ((PostItemDefaultView) dynamicView).setUpView(item);
            return;
        }
        if(dynamicView instanceof PostItemTextView){
            ((PostItemTextView) dynamicView).setUpView(item);
            return;
        }
        if(dynamicView instanceof PostItemImageView){
            ((PostItemImageView) dynamicView).setUpView(item);
            return;
        }
        if(dynamicView instanceof PostItemGifView){
            //todo
            ((PostItemGifView) dynamicView).setUpView(item);
            return;
        }
        if(dynamicView instanceof PostItemGalleryView){
            //todo
            ((PostItemGalleryView) dynamicView).setUpView(item);
            return;
        }
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
            lp.setMargins(25, actionBarHeight+50, 25, 50);
            this.setLayoutParams(lp);
        }
    }
    /**
     * other items
     */
    private void resolveOtherItemsDimensions() {
        //since the recycler view reuses the same items, make sure every other item is set to the intended margins
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(this.getLayoutParams());
        lp.setMargins(25, 0, 25, 50);
        this.setLayoutParams(lp);
    }

}
