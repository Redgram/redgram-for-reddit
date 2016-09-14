package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.data.managers.presenters.ProfileOverviewPresenter;
import com.matie.redgram.data.managers.presenters.ProfileOverviewPresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.profile.views.ProfileOverviewView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-07-26.
 */
@Module
public class ProfileOverviewModule {
    private ProfileOverviewView profileOverviewView;

    public ProfileOverviewModule(ProfileOverviewView profileOverviewView) {
        this.profileOverviewView = profileOverviewView;
    }

    @FragmentScope
    @Provides
    public ProfileOverviewView provideView(){return profileOverviewView;}

    @FragmentScope
    @Provides
    public ProfileOverviewPresenter provideProfileOverviewPresenter(App app){
        return new ProfileOverviewPresenterImpl(profileOverviewView, app);
    }
}
