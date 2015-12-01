package com.matie.redgram.ui.common.views.widgets.subreddit;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.matie.redgram.data.models.main.items.SubredditItem;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditViewHolder extends RecyclerView.ViewHolder{

    SubredditItemView subredditItemView;

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
}
