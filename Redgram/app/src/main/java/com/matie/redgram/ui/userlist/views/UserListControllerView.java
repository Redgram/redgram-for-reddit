package com.matie.redgram.ui.userlist.views;

import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;

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
