package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

/**
 * Created by matie on 2016-09-15.
 */
public interface UserListPresenter extends BasePresenter{
    void getUsers();
    void removeUser(String id, int position);
    void selectUser(String id, int position);
}
