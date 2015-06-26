package com.matie.redgram.ui.home.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.home.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemHeaderView extends PostBaseView {

    @InjectView(R.id.header_title_view)
    TextView headerTitleView;
    @InjectView(R.id.header_time_view)
    TextView headerTimeView;

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
    public void setUpView(PostItem item) {

        String score = item.getScore()+"";

        String author="";
        if(item.distinguished() != null){
            if(item.distinguished().equals("moderator")){
                author = item.getAuthor() + " [M]";
            }else if(item.distinguished().equals("admin")){
                author = item.getAuthor() + " [A]";
            }
            //todo: special tag
        }else{
            author = item.getAuthor();
        }

        String subreddit = "/r/"+item.getSubreddit();

        headerTitleView.setText(score + " " + res.getString(R.string.text_bullet)+ " " + author + " " +
                                                res.getString(R.string.text_bullet) + " " + subreddit);
        headerTimeView.setText(item.getTime() + " hrs");
    }
}
