package com.matie.redgram.ui.common.views.widgets.postlist;

import android.support.v7.widget.RecyclerView;

import com.matie.redgram.ui.links.views.LinksView;

/**
 * Created by matie on 04/04/15.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    LinksView linksViewListener;
    PostItemView itemView;

    public PostViewHolder(PostItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public PostViewHolder(PostItemView itemView, LinksView listener) {
        super(itemView);
        this.itemView = itemView;
        this.linksViewListener = listener;

        itemView.setListener(linksViewListener);
    }

    public PostItemView getItemView() {
        return itemView;
    }

    public void setItemView(PostItemView itemView) {
        this.itemView = itemView;
    }

}
