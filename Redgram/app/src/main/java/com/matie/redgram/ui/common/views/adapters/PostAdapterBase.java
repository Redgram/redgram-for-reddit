package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostItemView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostViewHolder;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class PostAdapterBase extends RecyclerView.Adapter<PostViewHolder>{

    private final int layoutResId;
    private List<PostItem> items = Collections.emptyList();
    private SingleLinkView singleLinkView;


    PostAdapterBase(Context context, int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public void replaceWith(List<PostItem> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void setPostItemListener(SingleLinkView singleLinkView) {
        this.singleLinkView = singleLinkView;
    }

    public PostItem getItem(int position){
        return items.get(position);
    }

    public List<PostItem> getItems() {
        return items;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        PostItemView v =
                (PostItemView) LayoutInflater.from(context).inflate(layoutResId, parent, false);

        View dynamicView = getDynamicView(viewType, v.getDynamicView(), v.getDynamicParent());

        if (dynamicView != null) {
            if (!dynamicView.equals(v.getDynamicView())) {
                v.getDynamicParent().removeAllViews();
                v.getDynamicParent().addView(dynamicView);
            }

            v.setDynamicView(dynamicView);
        }

        if (singleLinkView != null) {
           return new PostViewHolder(v, singleLinkView);
        }

        return new PostViewHolder(v);
    }

    //has to be implemented in sub-class and will inflate the specified view according to type
    public abstract View getDynamicView(int type, View dynamicView, ViewGroup dynamicParent);

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
         holder.getItemView().bindTo(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
