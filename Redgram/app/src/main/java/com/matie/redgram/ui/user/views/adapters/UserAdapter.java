package com.matie.redgram.ui.user.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.matie.redgram.R;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.widgets.drawer.UserItemView;
import com.matie.redgram.ui.common.views.widgets.drawer.UserViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final LayoutInflater inflater;
    private List<UserItem> userItems = Collections.emptyList();
    private UserListControllerView listener;

    public UserAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public UserAdapter(Context context, List<UserItem> userItems) {
        this.userItems = userItems;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserItemView itemView = (UserItemView) inflater.inflate(R.layout.user_item_view, parent, false);
        UserViewHolder viewHolder;
        if(listener != null){
            viewHolder = new  UserViewHolder(itemView, listener);
        }else{
            viewHolder = new UserViewHolder(itemView);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.getUserItemView().setup(getItem(position), position);
    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }

    public void replaceWith(List<UserItem> items) {
        this.userItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public List<UserItem> getItems(){
        return userItems;
    }

    public UserItem getItem(int position){
        return userItems.get(position);
    }


    public void setListener(UserListControllerView listener) {
        this.listener = listener;
    }

    public UserListControllerView getListener() {
        return listener;
    }
}
