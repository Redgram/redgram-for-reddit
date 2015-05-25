package com.matie.redgram.views.widgets.PostList;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by matie on 04/04/15.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    PostItemView itemView;

    public PostViewHolder(PostItemView itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public PostItemView getItemView() {
        return itemView;
    }

    public void setItemView(PostItemView itemView) {
        this.itemView = itemView;
    }

}
