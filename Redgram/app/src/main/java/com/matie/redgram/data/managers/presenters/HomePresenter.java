package com.matie.redgram.data.managers.presenters;

/**
 * Created by matie on 12/04/15.
 */
public interface HomePresenter {
    public void registerForEvents();
    public void unregisterForEvents();

    public void populateView();
}
