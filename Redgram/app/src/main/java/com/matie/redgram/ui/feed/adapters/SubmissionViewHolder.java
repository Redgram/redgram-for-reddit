package com.matie.redgram.ui.feed.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;

public abstract class SubmissionViewHolder extends RecyclerView.ViewHolder {

    public SubmissionViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(int position, SubmissionItem item);
}
