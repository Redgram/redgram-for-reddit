package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.main.views.MainView;

public class MainPresenterImpl extends BasePresenterImpl implements MainPresenter {

    private User user;

    public MainPresenterImpl(MainView mainView, App app) {
        super(mainView, app);
        // you could get a live realm copy and register changes in the UI - example
        user = databaseManager().getSessionUser();
    }

    @Override
    public User getSessionUser() {
        return user;
    }
}
