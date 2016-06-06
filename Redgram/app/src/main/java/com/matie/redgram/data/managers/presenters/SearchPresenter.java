package com.matie.redgram.data.managers.presenters;

/**
 * Search Presenter Interface
 */
public interface SearchPresenter {
    //these are used to determine the source of loading
    void registerForEvents();
    void unregisterForEvents();
}
