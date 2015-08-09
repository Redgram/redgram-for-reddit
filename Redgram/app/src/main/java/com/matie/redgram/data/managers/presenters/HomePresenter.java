package com.matie.redgram.data.managers.presenters;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matie on 12/04/15.
 */
public interface HomePresenter {
    public void registerForEvents();
    public void unregisterForEvents();

    public void getListing(String front, Map<String,String> params);
}
