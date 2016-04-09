package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.PostItem;

/**
 * Created by matie on 2016-02-10.
 */
public interface ThreadPresenter {
    void registerForEvents();
    void unregisterForEvents();

    void getThread(String id );
    void vote(PostItem item, int dir);
}
