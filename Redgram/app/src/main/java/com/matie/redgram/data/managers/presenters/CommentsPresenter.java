package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

import java.util.Map;

/**
 * Comments Presenter Interface
 */
public interface CommentsPresenter extends BasePresenter {
     void getComments(String article, Map<String,String> params);
     void getMoreComments();
}
