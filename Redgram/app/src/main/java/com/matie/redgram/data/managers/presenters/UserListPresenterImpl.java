package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.user.UserListView;
import com.matie.redgram.ui.common.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;

import javax.inject.Inject;

/**
 * Created by matie on 2016-09-15.
 */
public class UserListPresenterImpl implements UserListPresenter {

    @Inject
    public UserListPresenterImpl(UserListControllerView userListView, BaseContextView contentView, App app) {

    }
}
