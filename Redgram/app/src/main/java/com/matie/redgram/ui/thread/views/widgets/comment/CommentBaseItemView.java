package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * common click events
 *
 * Created by matie on 2016-01-30.
 */
public class CommentBaseItemView extends CardView{

    //inflate views here based on type
    @InjectView(R.id.container)
    FrameLayout container;

    View dynamicView;

    public CommentBaseItemView(Context context) {
        super(context);
    }

    public CommentBaseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentBaseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void setUp(CommentBaseItem item){
        requestLayout();
        ((CommentItemView)dynamicView).setUpView(item);
    }

    public FrameLayout getContainer() {
        return container;
    }

    public View getDynamicView() {
        return dynamicView;
    }

    public void setDynamicView(View dynamicView) {
        this.dynamicView = dynamicView;
    }
}
