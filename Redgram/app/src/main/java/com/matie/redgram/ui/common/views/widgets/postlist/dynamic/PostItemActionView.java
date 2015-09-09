package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostBaseView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 04/04/15.
 */
public class PostItemActionView extends PostBaseView{

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

    public PostItemActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void setUpView(PostItem item) {
        scoreView.setText(item.getScore()+"");
    }
}
