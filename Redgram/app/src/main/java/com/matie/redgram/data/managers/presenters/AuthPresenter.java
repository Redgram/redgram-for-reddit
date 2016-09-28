package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

/**
 * Authentication Presenter Interface
 */
public interface AuthPresenter extends BasePresenter {
    void getAccessToken(String url);
}
