package com.matie.redgram.ui.common.auth;

import android.app.Activity;

import com.matie.redgram.data.managers.presenters.AuthPresenter;
import com.matie.redgram.data.managers.presenters.AuthPresenterImpl;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.auth.views.AuthView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-02-21.
 */
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
    public AuthPresenter provideAuthPresenter(App app){
        return new AuthPresenterImpl(authView, app);
    }

}
