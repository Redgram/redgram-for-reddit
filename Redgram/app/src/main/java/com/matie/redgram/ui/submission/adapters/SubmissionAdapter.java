package com.matie.redgram.ui.submission.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;

import java.util.Collections;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {

    protected static final int TYPE_POST = 0;
    protected static final int TYPE_COMMENT = 1;

    private List<SubmissionItem> items = Collections.emptyList();

    @Override
    public SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (TYPE_POST == viewType) {

        }
        return null;
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
        } // TODO: 2017-12-18  Add more types

        return -1;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
