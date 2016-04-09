package com.matie.redgram.ui.thread.views.widgets;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.matie.redgram.ui.thread.views.ThreadView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 2016-04-04.
 */
public class OptionsView extends RelativeLayout implements CustomSpanListener {
    @InjectView(R.id.score)
    TextView scoreView;
    @InjectView(R.id.username)
    TextView userNameView;
    @InjectView(R.id.time)
    TextView timeView;
    @InjectView(R.id.action_profile)
    ImageView profileView;
    @InjectView(R.id.action_subreddit)
    ImageView subredditView;
    @InjectView(R.id.action_browser)
    ImageView browserView;
    @InjectView(R.id.action_copy)
    ImageView copyView;
    @InjectView(R.id.action_share)
    ImageView shareView;
    @InjectView(R.id.action_favorite)
    ImageView favView;
    @InjectView(R.id.action_hide)
    ImageView hideView;

    int position;
    ThreadView listener;

    public OptionsView(Context context) {
        super(context);
    }

    public OptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setup(PostItem postItem, ThreadView listener){
        this.listener = listener;
        userNameView.setText(postItem.getAuthor());
        scoreView.setText(postItem.getScore()+"");
        setupInfo(postItem);
    }

    public void setup(PostItem postItem, int position){
        this.position = position;
    }

    private void setupInfo(PostItem item) {
        String subreddit = "/r/"+item.getSubreddit();
        CustomClickable clickable = new CustomClickable(this, true);

        StringUtils.newSpannableBuilder(getContext())
                .setTextView(timeView)
                .append(item.getTime() + " hrs ago to ")
                .append(subreddit, clickable, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .span(new ForegroundColorSpan(Color.rgb(204, 0, 0)), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .clickable()
                .build();
    }

    @Override
    public void onClickableEvent(CharSequence targetString) {
        String target = targetString.toString();
        if(target.contains("/r/")){
            listener.visitSubreddit();
        }
    }

    @OnClick(R.id.action_hide)
    public void onHideClick(){
        listener.hidePost();
    }

    @OnClick(R.id.action_favorite)
    public void onFavClick(){
        listener.savePost();
    }

    @OnClick(R.id.action_copy)
    public void onCopyClick(){
        listener.copyItemLink();
    }

    @OnClick(R.id.action_share)
    public void onShareClick(){
        listener.sharePost();
    }

    @OnClick(R.id.action_browser)
    public void onBrowserClick(){
        listener.openInBrowser();
    }

    @OnClick(R.id.action_subreddit)
    public void onSubClick(){
        listener.visitSubreddit();
    }

    @OnClick(R.id.action_profile)
    public void onProfileClick(){
        listener.visitProfile();
    }
}
