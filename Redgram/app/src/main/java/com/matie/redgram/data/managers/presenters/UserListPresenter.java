package com.matie.redgram.data.managers.presenters;

/**
 * Created by matie on 2016-09-15.
 */
public interface UserListPresenter{
    void registerForEvents();
    void unregisterForEvents();
    void getUsers();
    void addUser(String username);
    void removeUser(String username);
    void selectUser(String username);
    void closeConnection();
}
