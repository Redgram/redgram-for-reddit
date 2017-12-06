package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

public interface ProfileAboutPresenter extends BasePresenter {
    boolean isAuthUser(String username);
    void getUserDetails(String username);
}
