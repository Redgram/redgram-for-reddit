package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-02-12.
 */
public class CommentMoreItemView extends CommentItemView {

    @InjectView(R.id.more_body)
    TextView textView;

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
        CommentMoreItem moreItem = (CommentMoreItem)item;
        textView.setText(getResources().getString(R.string.load_more) + " ( "+moreItem.getCount()+" )");
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }
}
