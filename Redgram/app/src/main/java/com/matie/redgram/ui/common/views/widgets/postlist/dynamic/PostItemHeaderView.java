package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;

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
    public void setUpView(App app, PostItem item) {

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

        headerUsernameView.setText(author);
        headerTimeSubredditView.setText("submitted " + item.getTime() + " hrs ago to " + subreddit);
    }
}
