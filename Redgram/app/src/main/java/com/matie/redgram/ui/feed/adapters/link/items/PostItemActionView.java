package com.matie.redgram.ui.feed.adapters.link.items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.ui.feed.links.views.SingleLinkView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
    SingleLinkView listener;

    public PostItemActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setupView(PostItem item, int position, SingleLinkView listener) {
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

    @OnClick(R.id.action_share)
    public void onActionShare(){
        listener.sharePost(getContext(), position);
    }

    @OnClick(R.id.action_vote_up)
    public void onUpVoteClick(){
        if (!TRUE.equalsIgnoreCase(postItem.getLikes())){
            listener.votePost(position, SingleLinkView.UP_VOTE);
        }else{
            listener.votePost(position, SingleLinkView.UN_VOTE);
        }
    }

    @OnClick(R.id.action_vote_down)
    public void onDownVoteClick(){
        if (!FALSE.equalsIgnoreCase(postItem.getLikes())){
            listener.votePost(position, SingleLinkView.DOWN_VOTE);
        }else{
            listener.votePost(position, SingleLinkView.UN_VOTE);
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
