package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;

/**
 * Authentication Presenter Interface
 */
public interface AuthPresenter extends BasePresenter {
    void getAccessToken(String url);
    void getAccessToken();
    void updateSession(AuthWrapper wrapper);
}
