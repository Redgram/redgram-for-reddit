package com.matie.redgram.data.managers.presenters;

/**
 * Subscription Presenter Interface
 */
public interface SubscriptionPresenter {
    void registerForEvents();
    void unregisterForEvents();

    void getSubreddits(boolean forceNetwork);
}
