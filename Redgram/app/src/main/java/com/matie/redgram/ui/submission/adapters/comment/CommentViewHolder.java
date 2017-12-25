package com.matie.redgram.ui.submission.adapters.comment;

import android.view.View;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;
import com.matie.redgram.ui.submission.adapters.SubmissionViewHolder;
import com.matie.redgram.ui.submission.adapters.comment.items.CommentBaseItemView;
import com.matie.redgram.ui.submission.adapters.comment.items.CommentItemView;
import com.matie.redgram.ui.submission.adapters.comment.items.CommentMoreItemView;
import com.matie.redgram.ui.submission.adapters.comment.items.CommentRegularItemView;
import com.matie.redgram.ui.thread.views.CommentsView;

import java.util.HashMap;

public class CommentViewHolder extends SubmissionViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    //main parent view
    private CommentBaseItemView commentBaseItemView;
    private CommentsView commentListener;


    public CommentViewHolder(CommentBaseItemView itemView) {
        super(itemView);
        this.commentBaseItemView = itemView;
    }

    public CommentViewHolder(CommentBaseItemView itemView, CommentsView listener) {
        super(itemView);

        this.commentBaseItemView = itemView;
        this.commentListener = listener;

        commentBaseItemView.setOnClickListener(this);
        commentBaseItemView.setOnLongClickListener(this);
    }

    @Override
    public void bind(int position, SubmissionItem item) {
        if (commentBaseItemView == null) return;

        commentBaseItemView.setUp((CommentBaseItem) item, position, new HashMap<>());
    }

    public CommentBaseItemView getCommentItemView() {
        return commentBaseItemView;
    }

    @Override
    public void onClick(View v) {
        CommentBaseItemView view = (CommentBaseItemView)v;
        resolveCommentType(view);
    }

    @Override
    public boolean onLongClick(View v) {
        if (commentListener == null) return false;

        CommentBaseItemView view = (CommentBaseItemView)v;
        commentListener.collapseItem(view.getItemPosition());
        return true;
    }

    private void resolveCommentType(CommentBaseItemView v) {
        if (commentListener == null) return;

        CommentItemView commentItemView = (CommentItemView)v.getDynamicView();

        if (commentItemView instanceof CommentRegularItemView) {
            resolveExpandCollapse(commentItemView, v.getItemPosition());
        } else if (commentItemView instanceof CommentMoreItemView) {
            loadMore(commentItemView, v.getItemPosition());
        }
    }

    private void loadMore(CommentItemView v, int itemPosition) {
        commentListener.loadMore(itemPosition);
    }

    private void resolveExpandCollapse(CommentItemView v, int position) {
        CommentRegularItemView targetView = (CommentRegularItemView) v;

        //toggles between expand/collapse
        boolean isExpanded = targetView.getIndicator().isExpanded();

        if (isExpanded) {
            commentListener.collapseItem(position);
        } else {
            commentListener.expandItem(position);
        }
    }


}
