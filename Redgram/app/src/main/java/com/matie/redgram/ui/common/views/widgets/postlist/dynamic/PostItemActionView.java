package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.content.Context;
import android.util.AttributeSet;
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

    @InjectView(R.id.comments_action_button)
    TextView commentsActionButton;
    @InjectView(R.id.source_action_view)
    TextView sourceActionView;

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
        commentsActionButton.setText(item.getNumComments()+ " comments");
        sourceActionView.setText("("+item.getUrl()+")");
    }
}
