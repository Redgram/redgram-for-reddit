package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.SubredditItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matie on 12/04/15.
 */
public interface HomePresenter {
    void registerForEvents();
    void unregisterForEvents();
    void getHomeViewWrapper();
    List<String> getSubreddits();

}
