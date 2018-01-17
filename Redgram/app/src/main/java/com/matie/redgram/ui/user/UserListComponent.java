package com.matie.redgram.ui.user;

import com.matie.redgram.data.managers.presenters.UserListPresenter;
import com.matie.redgram.ui.user.views.UserListControllerView;

import dagger.Subcomponent;

@Subcomponent(modules = UserListModule.class)
public interface UserListComponent {
    void inject(UserListView userListView);

    UserListPresenter getUserListPresenter();
    UserListControllerView getUserListControllerView();
}
