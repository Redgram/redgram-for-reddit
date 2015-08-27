package com.matie.redgram.data.managers.presenters;

import java.util.Map;

/**
 * Created by matie on 28/06/15.
 */
public interface SearchPresenter {
    //these are used to determine the source of loading
    public static final int REFRESH = 0;
    public static final int LOAD_MORE = 1;

    public void registerForEvents();
    public void unregisterForEvents();
    public void executeSearch(String query, Map<String, String> params);
    public void loadMoreResults(String query, Map<String, String> params);
}
