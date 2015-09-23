package com.matie.redgram.data.managers.presenters;

import java.util.HashMap;
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

    public void getListing(String front, Map<String,String> params);
    public void getMoreListing(String front, Map<String,String> params);
}
