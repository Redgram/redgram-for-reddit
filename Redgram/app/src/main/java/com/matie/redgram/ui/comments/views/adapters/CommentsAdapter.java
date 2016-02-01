package com.matie.redgram.ui.comments.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;
import com.matie.redgram.ui.comments.views.widgets.comment.CommentViewHolder;
import com.matie.redgram.ui.common.views.adapters.ExpandableAdapter;

import java.util.List;

/**
 * Created by matie on 2016-01-31.
 */
public class CommentsAdapter extends ExpandableAdapter<CommentViewHolder, CommentViewHolder> {

    public CommentsAdapter() {

    }


    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildCount(int groupPosition) {
        return 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public CommentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public CommentViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindGroupViewHolder(CommentViewHolder holder, int groupPosition, int viewType) {

    }

    @Override
    public void onBindChildViewHolder(CommentViewHolder holder, int groupPosition, int childPosition, int viewType) {

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(CommentViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return false;
    }
}
