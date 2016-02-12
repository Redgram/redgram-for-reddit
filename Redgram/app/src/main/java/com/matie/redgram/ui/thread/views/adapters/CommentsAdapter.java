package com.matie.redgram.ui.thread.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by matie on 2016-01-31.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private List<CommentBaseItem> commentItems = Collections.emptyList();

    public CommentsAdapter() {

    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void replaceWith(List<CommentBaseItem> items){
        commentItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    private CommentViewHolder getCommonViewHolder(ViewGroup parent) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CommentItemView v = (CommentItemView)inflater.inflate(R.layout.comment_item_view, parent, false);
        return new CommentViewHolder(v);
    }




}
