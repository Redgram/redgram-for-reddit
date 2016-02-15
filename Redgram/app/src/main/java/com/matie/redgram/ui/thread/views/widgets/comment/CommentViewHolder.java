package com.matie.redgram.ui.thread.views.widgets.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.annotation.ExpandableItemStateFlags;
import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.common.views.widgets.ExpandableIndicator;

/**
 * Created by matie on 2016-01-30.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    @ExpandableItemStateFlags
    private int flags;

    //main parent view
    private CommentBaseItemView commentBaseItemView;
    private CommentListener commentListener;


    public CommentViewHolder(CommentBaseItemView itemView) {
        super(itemView);
        this.commentBaseItemView = itemView;
    }

    public CommentViewHolder(CommentBaseItemView itemView, CommentListener listener) {
        super(itemView);
        this.commentBaseItemView = itemView;
        this.commentListener = listener;

        commentBaseItemView.setOnClickListener(this);
        commentBaseItemView.setOnLongClickListener(this);
    }

    public CommentBaseItemView getCommentItemView() {
        return commentBaseItemView;
    }

    public void setCommentItemView(CommentBaseItemView commentBaseItemView) {
        this.commentBaseItemView = commentBaseItemView;
    }

    @Override
    public void onClick(View v) {
        CommentBaseItemView view = (CommentBaseItemView)v;
        commentListener.onClick(view.getItem());
    }

    @Override
    public boolean onLongClick(View v) {
        CommentBaseItemView view = (CommentBaseItemView)v;
        commentListener.onLongClick(view.getItem());
        return true;
    }

    public interface CommentListener{
        void onClick(CommentBaseItem v);
        void onLongClick(CommentBaseItem v);
    }
}
