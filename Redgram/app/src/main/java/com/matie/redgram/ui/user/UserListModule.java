package com.matie.redgram.ui.user;

import com.matie.redgram.data.managers.presenters.UserListPresenter;
import com.matie.redgram.data.managers.presenters.UserListPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.user.views.UserListControllerView;

import dagger.Module;
import dagger.Provides;

@Module
public class UserListModule {

    private UserListControllerView userListView;
    private ContentView contentView;

    //flag to enable showing the Guest user in the list, if any presented
    private boolean enableDefault = false;

    public UserListModule(UserListControllerView userListView, ContentView contentView){
        this.userListView = userListView;
        this.contentView = contentView;
    }

    public UserListModule(UserListControllerView userListView, ContentView contentView, boolean enableDefault){
        this.userListView = userListView;
        this.contentView = contentView;
        this.enableDefault = enableDefault;
    }


    @Provides
    public UserListControllerView providesUserListView(){
        return userListView;
    }

    @Provides
    public UserListPresenter providesUserListPresenter(DatabaseManager databaseManager,
                                                       RedditAuthInterface redditAuthClient) {
        return new UserListPresenterImpl(userListView, contentView, databaseManager, redditAuthClient, enableDefault);
    }

}
