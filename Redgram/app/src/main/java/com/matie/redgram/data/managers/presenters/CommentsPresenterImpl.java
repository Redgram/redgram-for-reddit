package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.thread.views.CommentsView;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by matie on 2016-02-11.
 */
public class CommentsPresenterImpl implements CommentsPresenter {

    @Inject
    public CommentsPresenterImpl(CommentsView commentsView, App app) {
    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }

    //refresh?
    @Override
    public void getComments(String article, Map<String, String> params) {

    }

    @Override
    public void getMoreComments() {

    }
}
