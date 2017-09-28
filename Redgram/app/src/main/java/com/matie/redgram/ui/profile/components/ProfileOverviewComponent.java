package com.matie.redgram.ui.profile.components;

import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.profile.modules.ProfileAboutModule;

import dagger.Component;

/**
 * Created by matie on 2016-07-26.
 */
@FragmentScope
@Component(modules = {ProfileAboutModule.class},
            dependencies = ProfileComponent.class)
public interface ProfileOverviewComponent {
    void inject(ProfileOverviewFragment fragment);
    ProfileOverviewView getProfileOverviewView();
    ProfileOverviewView getProfileOverviewPresenter();
}
