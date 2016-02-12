package com.matie.redgram.data.managers.presenters;

/**
 * Created by matie on 2016-02-10.
 */
public interface ThreadPresenter {
    void registerForEvents();
    void unregisterForEvents();

    void getThread(String id );
}
