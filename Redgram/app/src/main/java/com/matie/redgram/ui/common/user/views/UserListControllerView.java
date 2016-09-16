package com.matie.redgram.ui.common.user.views;

import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;

/**
 * Created by matie on 2016-09-15.
 */
public interface UserListControllerView extends ContentView {
    void addAccount();
    void addAccount(String username);
    void selectAccount(int position);
    void removeAccount(int position);
    void close();
    UserItem getItem(int position);
    List<UserItem> getItems();
}
