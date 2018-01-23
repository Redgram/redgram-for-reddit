package com.matie.redgram.ui.common.views.widgets.drawer;

import android.support.v7.widget.RecyclerView;

import com.matie.redgram.ui.userlist.views.UserListControllerView;

/**
 * Created by matie on 2016-05-04.
 */
public class UserViewHolder extends RecyclerView.ViewHolder {

    private UserItemView userItemView;
    private UserListControllerView userListView;

    public UserViewHolder(UserItemView itemView) {
        super(itemView);
        this.userItemView = itemView;
    }

    public UserViewHolder(UserItemView itemView, UserListControllerView userListView) {
        super(itemView);
        this.userItemView = itemView;
        this.userListView = userListView;

        userItemView.setListener(userListView);
    }

    public UserItemView getUserItemView() {
        return userItemView;
    }

    public void setUserItemView(UserItemView userItemView) {
        this.userItemView = userItemView;
    }
}
