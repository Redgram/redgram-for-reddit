package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemHeaderView extends PostItemSubView {

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

        String score = item.getScore()+"";

        String author= item.getAuthor();
        if(item.distinguished() != null){
            if(item.distinguished().equals("moderator")){
                author = author + " [M]";
            }else if(item.distinguished().equals("admin")){
                author = author + " [A]";
            }
            //todo: special tag
        }

        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.BLUE);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        List<StringUtils.SpanContainer> containers = new ArrayList<StringUtils.SpanContainer>();
        containers.add(StringUtils.newSpanContainer(fcs, Spannable.SPAN_INCLUSIVE_INCLUSIVE));

        StringUtils.newSpannableBuilder(getContext())
                .setTextView(headerUsernameView)
                .appendList(author, containers)
                .build();

        String subreddit = "/r/"+item.getSubreddit();

//        headerUsernameView.setText(author);
        headerTimeSubredditView.setText("submitted " + item.getTime() + " hrs ago to " + subreddit);
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }
}
