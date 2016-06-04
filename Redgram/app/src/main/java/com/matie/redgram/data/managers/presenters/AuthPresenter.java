package com.matie.redgram.data.managers.presenters;

/**
 * Authentication Presenter Interface
 */
public interface AuthPresenter {
    void registerForEvents();
    void unregisterForEvents();
    void getAccessToken(String url);
}
