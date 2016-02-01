package com.matie.redgram.ui.comments.views.widgets.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.annotation.ExpandableItemStateFlags;

/**
 * Created by matie on 2016-01-30.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder implements ExpandableItemViewHolder{

    @ExpandableItemStateFlags
    private int flags;
    private CommentItemView commentItemView;


    public CommentViewHolder(CommentItemView itemView) {
        super(itemView);
        this.commentItemView = itemView;
    }

    public CommentItemView getCommentItemView() {
        return commentItemView;
    }

    public void setCommentItemView(CommentItemView commentItemView) {
        this.commentItemView = commentItemView;
    }

    @Override
    public void setExpandStateFlags(int flags) {
        this.flags = flags;
    }

    @Override
    @ExpandableItemStateFlags
    public int getExpandStateFlags() {
        return flags;
    }
}
