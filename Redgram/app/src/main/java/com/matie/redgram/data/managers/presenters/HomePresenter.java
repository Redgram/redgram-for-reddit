package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.SubredditItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by matie on 12/04/15.
 */
public interface HomePresenter {
    //these are used to determine the source of loading
    public static final int REFRESH = 0;
    public static final int LOAD_MORE = 1;

    public void registerForEvents();
    public void unregisterForEvents();

    public void getHomeViewWrapper();
    public List<String> getSubreddits();

    public void getListing(String subreddit, String front, Map<String,String> params);
    public void getMoreListing(String subreddit, String front, Map<String,String> params);

}
