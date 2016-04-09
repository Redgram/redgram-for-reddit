package com.matie.redgram.ui.common.views.widgets.subreddit;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.ui.common.views.adapters.PostAdapter;
import com.matie.redgram.ui.common.views.adapters.SubredditAdapter;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditRecyclerView extends RecyclerView {
    private final Context context;
    private final RecyclerView.LayoutManager layoutManager;
    private final SubredditAdapter subredditAdapter;

    public SubredditRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.layoutManager = new LinearLayoutManager(context);
        this.subredditAdapter = new SubredditAdapter(context);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        ButterKnife.inject(this);

        setLayoutManager(layoutManager);
        setAdapter(subredditAdapter);
    }

    public void replaceWith(List<SubredditItem> items){
        subredditAdapter.replaceWith(items);
    }

    public void setAdapterListener(SubredditViewHolder.SubredditViewHolderListener listener){
        subredditAdapter.setViewHolderListener(listener);
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public Adapter getAdapter() {
        return subredditAdapter;
    }

}
