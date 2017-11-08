package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

/**
 * Created by matie on 2016-07-26.
 */
public interface ProfileAboutPresenter extends BasePresenter {
    boolean isAuthUser(String username);
    void getUserDetails(String username);
}