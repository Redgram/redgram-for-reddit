package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.data.managers.presenters.ProfileAboutPresenter;
import com.matie.redgram.data.managers.presenters.ProfileAboutPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.scopes.FragmentScope;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-07-26.
 */
@Module
public class ProfileAboutModule {
    private ProfileAboutView profileAboutView;

    public ProfileAboutModule(ProfileAboutView profileAboutView) {
        this.profileAboutView = profileAboutView;
    }

    @FragmentScope
    @Provides
    public ProfileAboutView provideView(){return profileAboutView;}

    @FragmentScope
    @Provides
    public ProfileAboutPresenter provideProfileAboutPresenter(App app){
        return new ProfileAboutPresenterImpl(app, profileAboutView);
    }
}
