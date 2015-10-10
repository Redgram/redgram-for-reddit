package com.matie.redgram.ui.common.views.widgets.postlist.dynamic;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 19/05/15.
 */
public class PostItemAnimatedView extends PostItemSubView {
    @InjectView(R.id.gif_text_view)
    PostItemTextView postItemTextView;

    public PostItemAnimatedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }
    @Override
    public void setupView(PostItem item) {
        postItemTextView.setupView(item);
    }

    @Override
    public void handleNsfwUpdate(boolean disabled) {

    }
}
