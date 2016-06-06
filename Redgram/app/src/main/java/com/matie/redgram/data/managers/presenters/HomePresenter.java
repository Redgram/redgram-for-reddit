package com.matie.redgram.data.managers.presenters;

import java.util.List;

/**
 * Home Presenter Interface
 */
public interface HomePresenter {
    void registerForEvents();
    void unregisterForEvents();
    void getHomeViewWrapper();
    List<String> getSubreddits();

}
