package com.matie.redgram.data.managers.presenters;

/**
 * Created by matie on 2016-02-21.
 */
public interface AuthPresenter {
    void registerForEvents();
    void unregisterForEvents();
    void getAccessToken(String url);
}
