package com.matie.redgram.data.managers.presenters;

/**
 * Created by matie on 28/06/15.
 */
public interface SearchPresenter {
    public void registerForEvents();
    public void unregisterForEvents();
    public void executeSearch(String query);
}
