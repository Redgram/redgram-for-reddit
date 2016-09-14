package com.matie.redgram.ui.common.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.widgets.postlist.PostItemView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostViewHolder;
import com.matie.redgram.ui.posts.views.LinksView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by matie on 04/04/15.
 */
public abstract class PostAdapterBase extends RecyclerView.Adapter<PostViewHolder>{

    private final Context context;
    private final int layoutResId;

    private final LayoutInflater inflater;
    private List<PostItem> items = Collections.emptyList();
    private LinksView linksView;


    public PostAdapterBase(Context context, int layoutResId){
        this.context = context;
        this.layoutResId = layoutResId;
        this.inflater = LayoutInflater.from(context);
    }

    public void replaceWith(List<PostItem> items){
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void setPostItemListener(LinksView linksView){
        this.linksView = linksView;
    }

    public PostItem getItem(int position){
        return items.get(position);
    }

    public List<PostItem> getItems() {
        return items;
    }

    public int getViewTypeCount() {
        return getTypeCount();
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    //has to be implemented in sub-class and will inflate the specified view according to type
    public abstract View getDynamicView(int type, View dynamicView, ViewGroup dynamicParent);

    public abstract int getItemType(int position);

    public abstract int getTypeCount();

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PostItemView v = (PostItemView)inflater.inflate(layoutResId, parent, false);

        View dynamicView = getDynamicView(viewType, v.getDynamicView(), v.getDynamicParent());

        if(!dynamicView.equals(v.getDynamicView())){
            v.getDynamicParent().removeAllViews();
            v.getDynamicParent().addView(dynamicView);
        }
        v.setDynamicView(dynamicView);

        if(linksView != null){
           return new PostViewHolder(v, linksView);
        }

        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Log.d("ITEM VIEW HOLDER" , holder+"");
//        Log.d("ITEM VIEW" , holder.getItemView()+"");
         holder.getItemView().bindTo(items.get(position), position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }


}
