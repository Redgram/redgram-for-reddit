package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.PostItem;

import java.util.Map;

/**
 * Thread Presenter Interface
 */
public interface ThreadPresenter {
    void registerForEvents();
    void unregisterForEvents();

    void getThread(String id, Map<String, String> params);
    void vote(PostItem item, int dir);
    void save(PostItem postItem, boolean save);
    void hide(PostItem postItem, boolean hide);
}
