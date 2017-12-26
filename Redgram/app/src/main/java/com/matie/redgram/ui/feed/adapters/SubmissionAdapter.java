package com.matie.redgram.ui.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {

    private List<SubmissionItem> items = Collections.emptyList();
    private SubmissionViewCreator viewHolderDelegate;

    public SubmissionAdapter(SubmissionViewCreator viewHolderDelegate) {
        this.viewHolderDelegate = viewHolderDelegate;
    }

    public SubmissionAdapter(SubmissionViewCreator viewHolderDelegate, List<SubmissionItem> items) {
        this(viewHolderDelegate);
        this.viewHolderDelegate = viewHolderDelegate;
        replaceWith(items);
    }

    @Override
    public SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewHolderDelegate == null) return null;
        return viewHolderDelegate.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(SubmissionViewHolder holder, int position) {
        holder.bind(position, items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (viewHolderDelegate == null) return -1;
        return viewHolderDelegate.getItemViewType(position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replaceWith(List<SubmissionItem> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public SubmissionItem getItem(int position) {
        return items.get(position);
    }

    public List<SubmissionItem> getItems() {
        return items;
    }

    public SubmissionViewCreator getViewHolderDelegate() {
        return viewHolderDelegate;
    }

    public void setViewHolderDelegate(SubmissionViewCreator viewHolderDelegate) {
        this.viewHolderDelegate = viewHolderDelegate;
    }
}
