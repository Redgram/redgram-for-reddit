package com.matie.redgram.ui.submission.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;

import java.util.Collections;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {

    public static final int TYPE_POST = 0;
    public static final int TYPE_COMMENT = 1;

    private List<SubmissionItem> items = Collections.emptyList();
    private SubmissionViewHolderDelegate viewHolderDelegate;

    public SubmissionAdapter() {
    }

    public SubmissionAdapter(SubmissionViewHolderDelegate viewHolderDelegate) {
        this();
        this.viewHolderDelegate = viewHolderDelegate;
    }

    public SubmissionAdapter(List<SubmissionItem> items, SubmissionViewHolderDelegate viewHolderDelegate) {
        this(viewHolderDelegate);
        this.viewHolderDelegate = viewHolderDelegate;
    }

    @Override
    public SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewHolderDelegate.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(SubmissionViewHolder holder, int position) {
        holder.bind(position, items.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        final SubmissionItem item = items.get(position);

        if (item instanceof PostItem) {
            return TYPE_POST;
        } else if (item instanceof CommentBaseItem) {
            return TYPE_COMMENT;
        }

        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public SubmissionItem getItem(int position) {
        return items.get(position);
    }

    public List<SubmissionItem> getItems() {
        return items;
    }

    public SubmissionViewHolderDelegate getViewHolderDelegate() {
        return viewHolderDelegate;
    }

    public void setViewHolderDelegate(SubmissionViewHolderDelegate viewHolderDelegate) {
        this.viewHolderDelegate = viewHolderDelegate;
    }
}
