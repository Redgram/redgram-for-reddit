package com.matie.redgram.ui.common.user;

import com.matie.redgram.data.managers.presenters.UserListPresenter;
import com.matie.redgram.ui.common.user.views.UserListControllerView;

import dagger.Subcomponent;

/**
 * Created by matie on 2016-09-15.
 */
@Subcomponent(modules = UserListModule.class)
public interface UserListComponent {
    void inject(UserListView userListView);

    UserListPresenter getUserListPresenter();
    UserListControllerView getUserListControllerView();
}
