package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.data.managers.presenters.ProfileActivityPresenter;
import com.matie.redgram.data.managers.presenters.ProfileActivityPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.scopes.FragmentScope;
import com.matie.redgram.ui.profile.views.ProfileActivityView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2017-09-27.
 */
@Module
public class ProfileActivityModule {
    private ProfileActivityView profileActivityView;

    public ProfileActivityModule(ProfileActivityView profileActivityView) {
        this.profileActivityView = profileActivityView;
    }

    @FragmentScope
    @Provides
    public ProfileActivityView provideView(){return profileActivityView;}

    @FragmentScope
    @Provides
    public ProfileActivityPresenter provideProfileActivityPresenter(App app){
        return new ProfileActivityPresenterImpl(app, profileActivityView);
    }
}
