package com.matie.redgram.ui.common.user;

import com.matie.redgram.data.managers.presenters.UserListPresenter;
import com.matie.redgram.data.managers.presenters.UserListPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-09-15.
 */
@Module
public class UserListModule {

    private UserListControllerView userListView;
    private BaseContextView contextView;

    public UserListModule(UserListControllerView userListView, BaseContextView contextView){
        this.userListView = userListView;
        this.contextView = contextView;
    }


    @Provides
    public UserListControllerView providesUserListView(){
        return userListView;
    }

    @Provides
    public UserListPresenter providesUserListPresenter(App app){
        return new UserListPresenterImpl(userListView, contextView, app);
    }

}
