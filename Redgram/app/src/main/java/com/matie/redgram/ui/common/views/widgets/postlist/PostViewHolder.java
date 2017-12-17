package com.matie.redgram.ui.common.views.widgets.postlist;

import android.support.v7.widget.RecyclerView;

import com.matie.redgram.ui.submission.links.views.SingleLinkView;


public class PostViewHolder extends RecyclerView.ViewHolder {

    private PostItemView itemView;

    public PostViewHolder(PostItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public PostViewHolder(PostItemView itemView, SingleLinkView listener) {
        super(itemView);
        this.itemView = itemView;

        itemView.setListener(listener);
    }

    public PostItemView getItemView() {
        return itemView;
    }

}
