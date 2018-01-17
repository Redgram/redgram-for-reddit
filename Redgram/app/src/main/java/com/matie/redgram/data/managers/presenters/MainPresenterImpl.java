package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.main.views.MainView;

import javax.inject.Inject;

public class MainPresenterImpl extends BasePresenterImpl implements MainPresenter {

    private User user;

    @Inject
    public MainPresenterImpl(MainView mainView, DatabaseManager databaseManager) {
        super(mainView, databaseManager);

        // you could get a live realm copy and register changes in the UI - example
        user = databaseManager.getSessionUser();
    }

    @Override
    public User getSessionUser() {
        return user;
    }
}
