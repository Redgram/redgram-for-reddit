package com.matie.redgram.data.managers.presenters;

import java.util.List;
import java.util.Map;

/**
 * Created by matie on 2016-02-11.
 */
public interface CommentsPresenter {
     void registerForEvents();
     void unregisterForEvents();

     void getComments(String article, Map<String,String> params);
     void getMoreComments();
}
