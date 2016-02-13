package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;

import butterknife.ButterKnife;

/**
 * Created by matie on 2016-02-12.
 */
public class CommentMoreItemView extends CommentItemView {
    public CommentMoreItemView(Context context) {
        super(context);
    }

    public CommentMoreItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentMoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setUpView(CommentBaseItem item) {

    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }
}
