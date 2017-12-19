package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;

/**
 * Created by matie on 2016-02-12.
 */
public abstract class CommentItemView extends RelativeLayout {
    public CommentItemView(Context context) {
        super(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract void setUpView(CommentBaseItem item);

}
