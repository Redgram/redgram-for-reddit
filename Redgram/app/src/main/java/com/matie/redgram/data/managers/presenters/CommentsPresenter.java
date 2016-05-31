package com.matie.redgram.data.managers.presenters;

import java.util.Map;

/**
 * Comments Presenter Interface
 */
public interface CommentsPresenter {
     void registerForEvents();
     void unregisterForEvents();

     void getComments(String article, Map<String,String> params);
     void getMoreComments();
}
