package com.matie.redgram.ui.thread.views.widgets.comment;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.annotation.ExpandableItemStateFlags;
import com.matie.redgram.R;
import com.matie.redgram.ui.common.views.widgets.ExpandableIndicator;

/**
 * Created by matie on 2016-01-30.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @ExpandableItemStateFlags
    private int flags;

    //main parent view
    private CommentItemView commentItemView;
    //child view
    private FrameLayout container;
    private ExpandableIndicator expandableIndicator;
    private TextView content;


    public CommentViewHolder(CommentItemView itemView) {
        super(itemView);
        this.commentItemView = itemView;
        this.container = (FrameLayout)itemView.findViewById(R.id.container);
        this.expandableIndicator = (ExpandableIndicator)itemView.findViewById(R.id.indicator);
        this.content = (TextView)itemView.findViewById(R.id.comment_text);
    }

    public CommentItemView getCommentItemView() {
        return commentItemView;
    }

    public void setCommentItemView(CommentItemView commentItemView) {
        this.commentItemView = commentItemView;
    }

    public FrameLayout getContainer() {
        return container;
    }

    public void setContainer(FrameLayout container) {
        this.container = container;
    }

    public ExpandableIndicator getExpandableIndicator() {
        return expandableIndicator;
    }

    public TextView getContent() {
        return content;
    }

}
