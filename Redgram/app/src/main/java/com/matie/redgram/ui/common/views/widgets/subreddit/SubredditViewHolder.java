package com.matie.redgram.ui.common.views.widgets.subreddit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.matie.redgram.data.models.main.items.SubredditItem;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

    SubredditItemView subredditItemView;
    SubredditViewHolderListener listener;

    public SubredditViewHolder(SubredditItemView subredditItemView, SubredditViewHolderListener listener) {
        super(subredditItemView);
        this.subredditItemView = subredditItemView;
        this.listener = listener;

        subredditItemView.setOnClickListener(this);
        subredditItemView.setOnLongClickListener(this);
    }

    public SubredditViewHolder(SubredditItemView subredditItemView) {
        super(subredditItemView);
        this.subredditItemView = subredditItemView;
    }

    public SubredditItemView getSubredditItemView() {
        return subredditItemView;
    }

    public void setSubredditItemView(SubredditItemView subredditItemView) {
        this.subredditItemView = subredditItemView;
    }


    @Override
    public void onClick(View v) {
        SubredditItemView itemView = (SubredditItemView)v;
        listener.onClick(itemView.getSubredditName());
    }

    @Override
    public boolean onLongClick(View v) {
        SubredditItemView itemView = (SubredditItemView)v;
        listener.onLongClick(itemView.getItem());
        return true;
    }

    public static interface SubredditViewHolderListener{
        public void onClick(String subredditName);
        public void onLongClick(SubredditItem item);
    }
}
