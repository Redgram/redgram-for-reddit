package com.matie.redgram.ui.thread.views.widgets.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.matie.redgram.ui.thread.views.CommentsView;

/**
 * Created by matie on 2016-01-30.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

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

    public CommentBaseItemView getCommentItemView() {
        return commentBaseItemView;
    }

    public void setCommentItemView(CommentBaseItemView commentBaseItemView) {
        this.commentBaseItemView = commentBaseItemView;
    }

    @Override
    public void onClick(View v) {
        CommentBaseItemView view = (CommentBaseItemView)v;
        resolveCommentType(view);
    }


    @Override
    public boolean onLongClick(View v) {
        CommentBaseItemView view = (CommentBaseItemView)v;
        commentListener.collapseItem(view.getItemPosition());
        return true;
    }

    private void resolveCommentType(CommentBaseItemView v) {
        CommentItemView commentItemView = (CommentItemView)v.getDynamicView();

        if(commentItemView instanceof CommentRegularItemView){
            resolveExpandCollapse(commentItemView, v.getItemPosition());
        }else if(commentItemView instanceof CommentMoreItemView){
            loadMore(commentItemView, v.getItemPosition());
        }
    }

    private void loadMore(CommentItemView v, int itemPosition) {
        CommentMoreItemView targetView = (CommentMoreItemView) v;
        commentListener.loadMore(itemPosition);
    }

    private void resolveExpandCollapse(CommentItemView v, int position) {
        CommentRegularItemView targetView = (CommentRegularItemView) v;
        //toggles between expand/collapse
        boolean isExpanded = targetView.getIndicator().isExpanded();
        if(isExpanded){
            commentListener.collapseItem(position);
        }else{
            commentListener.expandItem(position);
        }
    }


}
