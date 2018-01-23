package com.matie.redgram.ui.userlist;

import com.matie.redgram.data.managers.presenters.UserListPresenter;
import com.matie.redgram.ui.scopes.UserScope;
import com.matie.redgram.ui.userlist.views.UserListControllerView;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = UserListModule.class)
public interface UserListComponent {
    void inject(UserListView userListView);

    UserListPresenter getUserListPresenter();
    UserListControllerView getUserListControllerView();
}
