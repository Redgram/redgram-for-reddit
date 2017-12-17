package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.thread.views.CommentsView;

import java.util.Map;

import javax.inject.Inject;

public class CommentsPresenterImpl extends BasePresenterImpl implements CommentsPresenter {

    @Inject
    public CommentsPresenterImpl(CommentsView commentsView, App app) {
        super(commentsView, app);
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
