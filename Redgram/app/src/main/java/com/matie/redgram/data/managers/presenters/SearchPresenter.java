package com.matie.redgram.data.managers.presenters;

import java.util.Map;

/**
 * Created by matie on 28/06/15.
 */
public interface SearchPresenter {
    //these are used to determine the source of loading
    public void registerForEvents();
    public void unregisterForEvents();
}
