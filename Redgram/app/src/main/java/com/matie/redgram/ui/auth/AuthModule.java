package com.matie.redgram.ui.auth;

import android.app.Activity;

import com.matie.redgram.data.managers.presenters.AuthPresenter;
import com.matie.redgram.data.managers.presenters.AuthPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthClient;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.user.RedditClient;
import com.matie.redgram.data.network.api.reddit.user.RedditClientInterface;
import com.matie.redgram.ui.auth.views.AuthView;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class AuthModule {
    private final Activity activity;
    private final AuthView authView;

    public AuthModule(Activity activity) {
        this.activity = activity;
        this.authView = activity();
    }

    @ActivityScope
    @Provides
    AuthActivity activity(){
        return (AuthActivity)activity;
    }

    @ActivityScope
    @Provides
    public AuthView provideView(){return authView;}

    @ActivityScope
    @Provides
    public AuthPresenter provideAuthPresenter(DatabaseManager databaseManager,
                                              RedditAuthInterface authClient,
                                              RedditClientInterface client){
        return new AuthPresenterImpl(authView, databaseManager, authClient, client);
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

}
