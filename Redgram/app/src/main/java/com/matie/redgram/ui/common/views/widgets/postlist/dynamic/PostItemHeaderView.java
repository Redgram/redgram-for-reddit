package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.text.CustomClickable;
import com.matie.redgram.ui.common.utils.text.CustomClickableListener;
import com.matie.redgram.ui.common.utils.text.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemHeaderView extends PostItemSubView implements CustomClickableListener {

    @InjectView(R.id.header_username_view)
    TextView headerUsernameView;
    @InjectView(R.id.header_time_subreddit_view)
    TextView headerTimeSubredditView;
    @InjectView(R.id.header_more_view)
    ImageView headerMoreView;

    private  final Resources res;

    public PostItemHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item) {

        // TODO: 2015-12-14 click on user to view user activity
        setupAuthor(item);

        String subreddit = "/r/"+item.getSubreddit();
        CustomClickable clickable = new CustomClickable(this);

        StringUtils.newSpannableBuilder(getContext())
                .setTextView(headerTimeSubredditView)
                .append("submitted " + item.getTime() + " hrs ago to ")
                .append(subreddit, clickable, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .span(new ForegroundColorSpan(Color.rgb(204, 0, 0)), Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                .clickable()
                .build();
    }

    private void setupAuthor(PostItem item) {
        String author = item.getAuthor();
        if(item.distinguished() != null){
            ForegroundColorSpan fcs = null;
            final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
            if(item.distinguished().equals("moderator")){
                author = author + " [M]";
                fcs = new ForegroundColorSpan(Color.rgb(28, 139, 32));
            }else if(item.distinguished().equals("admin")){
                author = author + " [A]";
                fcs = new ForegroundColorSpan(Color.rgb(204, 0, 0));
            }else{
                author = author + " [" + item.distinguished()+ "]";
                fcs = new ForegroundColorSpan(Color.rgb(0, 102, 204));
            }

            StringUtils.newSpannableBuilder(getContext())
                    .setTextView(headerUsernameView)
                    .append(author)
                    .span(fcs, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    .span(bss, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                    .build();
        }else{
            headerUsernameView.setText(author);
        }
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @Override
    public void handleMainClickEvent() {

    }


    @Override
    public void onClickableEvent(CharSequence targetString) {
        Log.d("CLICKABLE", "onClick [" + targetString + "]");
        String target = targetString.toString();
        if(target.contains("/r/")){
            String subredditName = target.substring(target.lastIndexOf('/')+1, target.length());
            getMainActivity().openFragmentWithResult(subredditName);
        }
    }
}
