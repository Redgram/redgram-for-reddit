package com.matie.redgram.data.managers.presenters;

import java.util.List;

/**
 * Created by matie on 2015-11-29.
 */
public interface SubscriptionPresenter {
    public void registerForEvents();
    public void unregisterForEvents();

    public void getSubreddits(boolean forceNetwork);
}
