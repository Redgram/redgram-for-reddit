package com.matie.redgram.ui.common.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;

import java.util.List;

/**
 * Created by matie on 2016-01-31.
 */
public abstract class ExpandableAdapter<G extends RecyclerView.ViewHolder, C extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ExpandableItemAdapter<G, C> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {}

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onBindGroupViewHolder(G holder, int groupPosition, int viewType, List<Object> payloads) {
        onBindGroupViewHolder(holder, groupPosition, viewType);
    }

    @Override
    public void onBindChildViewHolder(C holder, int groupPosition, int childPosition, int viewType, List<Object> payloads) {
        onBindChildViewHolder(holder, groupPosition, childPosition, viewType);
    }

    //return true to customize behavior
    @Override
    public boolean onHookGroupCollapse(int groupPosition, boolean fromUser) {
        return true;
    }

    @Override
    public boolean onHookGroupExpand(int groupPosition, boolean fromUser) {
        return true;
    }
}