package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.posts.views.LinksView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemActionView extends PostItemSubView {
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    @InjectView(R.id.action_vote_up)
    ImageView voteUp;
    @InjectView(R.id.action_vote_down)
    ImageView voteDown;
    @InjectView(R.id.action_score_view)
    TextView scoreView;
    @InjectView(R.id.action_share)
    ImageView shareView;
    @InjectView(R.id.action_favorite)
    ImageView favoriteView;
    @InjectView(R.id.action_hide)
    ImageView hideView;

    PostItem postItem;
    int position;
    LinksView listener;

    public PostItemActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, LinksView listener) {
        this.position = position;
        postItem = item;
        this.listener = listener;
        scoreView.setText(item.getScore() + "");

        if(TRUE.equalsIgnoreCase(item.getLikes())){
            voteUp.setColorFilter(getResources().getColor(R.color.material_green700));
            voteDown.setColorFilter(getResources().getColor(R.color.material_grey700));
        }else if(FALSE.equalsIgnoreCase(item.getLikes())){
            voteDown.setColorFilter(getResources().getColor(R.color.material_red400));
            voteUp.setColorFilter(getResources().getColor(R.color.material_grey700));
        }else{
            voteUp.setColorFilter(getResources().getColor(R.color.material_grey700));
            voteDown.setColorFilter(getResources().getColor(R.color.material_grey700));
        }

        if(item.isSaved()){
            favoriteView.setColorFilter(getResources().getColor(R.color.material_red400));
        }else{
            favoriteView.setColorFilter(getResources().getColor(R.color.material_grey700));
        }

    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }

    @OnClick(R.id.action_share)
    public void onActionShare(){
        listener.sharePost(position);
    }

    @OnClick(R.id.action_vote_up)
    public void onUpVoteClick(){
        if (!TRUE.equalsIgnoreCase(postItem.getLikes())){
            voteUp.setColorFilter(getResources().getColor(R.color.material_green700));
            voteDown.setColorFilter(null);
            listener.votePost(position, listener.UP_VOTE);
        }else{
            voteUp.setColorFilter(null);
            listener.votePost(position, listener.UN_VOTE);
        }
    }

    @OnClick(R.id.action_vote_down)
    public void onDownVoteClick(){
        if (!FALSE.equalsIgnoreCase(postItem.getLikes())){
            voteDown.setColorFilter(R.color.material_red700);
            voteUp.setColorFilter(null);
            listener.votePost(position, listener.DOWN_VOTE);
        }else{
            voteDown.setColorFilter(null);
            listener.votePost(position, listener.UN_VOTE);
        }
    }

    @OnClick(R.id.action_favorite)
     public void onFavClick(){
        if(postItem.isSaved()){
            listener.savePost(position, false);
        }else{
            listener.savePost(position, true);
        }
    }

    @OnClick(R.id.action_hide)
    public void onHideClick(){
        listener.hidePost(position);
    }
}
