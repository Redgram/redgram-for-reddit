package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

/**
 * Subscription Presenter Interface
 */
public interface SubscriptionPresenter extends BasePresenter {
    void getSubreddits(boolean forceNetwork);
}
