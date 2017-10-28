package com.matie.redgram.ui.common.user.views;

import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.views.ContentSubView;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;

/**
 * Created by matie on 2016-09-15.
 */
public interface UserListControllerView extends ContentView {
    void populateView(List<UserItem> userList);
    void addAccount();
    void selectAccount(String id, int position);
    void removeAccount(String id, int position);
    void removeItem(int position);
    void restartContext();
    void close();
    UserItem getItem(int position);
    List<UserItem> getItems();
}
