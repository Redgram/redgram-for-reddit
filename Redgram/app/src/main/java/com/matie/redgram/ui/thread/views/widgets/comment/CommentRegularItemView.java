package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-02-12.
 */
public class CommentRegularItemView extends CommentItemView{

    @InjectView(R.id.reg_body)
    TextView textView;
    @InjectView(R.id.author)
    TextView authorView;
    @InjectView(R.id.time)
    TextView timeView;

    public CommentRegularItemView(Context context) {
        super(context);
    }

    public CommentRegularItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentRegularItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void setUpView(CommentBaseItem item) {
        CommentItem commentItem = (CommentItem)item;
        textView.setText(commentItem.getBody());
        authorView.setText(commentItem.getAuthor());
        timeView.setText(commentItem.getLevel()+"");
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

}
