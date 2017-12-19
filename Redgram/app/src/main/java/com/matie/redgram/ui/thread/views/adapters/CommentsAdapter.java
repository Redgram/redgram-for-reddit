package com.matie.redgram.ui.thread.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;
import com.matie.redgram.ui.thread.views.CommentsView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentBaseItemView;
import com.matie.redgram.ui.thread.views.widgets.comment.CommentViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by matie on 2016-01-31.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private static final int TYPE_REGULAR = 0;
    private static final int TYPE_MORE = 1;

    private final LayoutInflater inflater;
    private List<CommentBaseItem> commentItems = Collections.emptyList();
    private Map<Integer, Integer> colorMap = new HashMap<>();
    private CommentsView commentListener;

    public CommentsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final CommentBaseItemView v = (CommentBaseItemView)inflater.inflate(R.layout.comment_item_view, parent, false);

        View dynamicView = getCommentView(viewType, v.getDynamicView(), v.getContainer());

        if(!dynamicView.equals(v.getDynamicView())){
            v.getContainer().removeAllViews();
            v.getContainer().addView(dynamicView);
        }
        v.setDynamicView(dynamicView);

        if(commentListener != null){
            return new CommentViewHolder(v, commentListener);
        }

        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.getCommentItemView().setUp(getItem(position), position, getLevelToColorMap());
    }

    private View getCommentView(int viewType, View dynamicView, FrameLayout container) {
        if(dynamicView == null){
            dynamicView = inflateViewByType(viewType, dynamicView, container);
        }
        return dynamicView;
    }

    private View inflateViewByType(int viewType, View dynamicView, FrameLayout container) {
        switch (viewType){
            case TYPE_REGULAR:
                dynamicView = getInflater().inflate(R.layout.comment_item_regular_view, container, false);
                break;
            case TYPE_MORE:
                dynamicView = getInflater().inflate(R.layout.comment_item_more_item, container, false);
                break;
        }
        return dynamicView;
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        int id = TYPE_REGULAR; //default
        if(getItem(position).getCommentType() == CommentBaseItem.CommentType.REGULAR){
            return id;
        }
        if(getItem(position).getCommentType() == CommentBaseItem.CommentType.MORE){
            id = TYPE_MORE;
            return id;
        }
        return id;
    }

    public void setCommentListener(CommentsView commentListener) {
        this.commentListener = commentListener;
    }

    public void replaceWith(List<CommentBaseItem> items){
        commentItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public CommentBaseItem getItem(int position){
        return commentItems.get(position);
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    protected int getMaxLevel(){
        int max = 0;
        for(CommentBaseItem item : commentItems){
            if(item.getLevel() > max){
                max = item.getLevel();
            }
        }
        return max;
    }

    protected Map<Integer, Integer> getLevelToColorMap(){
        if(colorMap.isEmpty()){
            for (int i = 1; i <= getMaxLevel(); i++){
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                colorMap.put(i, color);
            }
        }
        return colorMap;
    }

    public List<CommentBaseItem> getCommentItems() {
        return commentItems;
    }
}
