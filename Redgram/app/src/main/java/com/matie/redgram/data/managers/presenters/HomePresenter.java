package com.matie.redgram.data.managers.presenters;

import java.util.List;

/**
 * Created by matie on 12/04/15.
 */
public interface HomePresenter {
    void registerForEvents();
    void unregisterForEvents();
    void getHomeViewWrapper();
    List<String> getSubreddits();

}
