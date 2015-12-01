package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditItemView;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditAdapter extends RecyclerView.Adapter<SubredditViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;

    private List<SubredditItem> items = Collections.emptyList();

    public SubredditAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void replaceWith(List<SubredditItem> items){
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @Override
    public SubredditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SubredditItemView itemView = (SubredditItemView)inflater.inflate(R.layout.subreddit_item_view, parent, false);
        return new SubredditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubredditViewHolder holder, int position) {
        holder.getSubredditItemView().bindTo(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
