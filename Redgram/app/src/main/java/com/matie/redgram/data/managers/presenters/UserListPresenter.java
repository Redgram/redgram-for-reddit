package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

public interface UserListPresenter extends BasePresenter{
    void getUsers();
    void removeUser(String id, int position);
    void selectUser(String id, int position);
    void switchUser(String id, int position);
    void switchUser();
}
