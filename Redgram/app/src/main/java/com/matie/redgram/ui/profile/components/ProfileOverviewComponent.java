package com.matie.redgram.ui.profile.components;

import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.profile.modules.ProfileOverviewModule;
import com.matie.redgram.ui.profile.ProfileOverviewFragment;
import com.matie.redgram.ui.profile.views.ProfileOverviewView;

import dagger.Component;

/**
 * Created by matie on 2016-07-26.
 */
@FragmentScope
@Component(modules = {ProfileOverviewModule.class},
            dependencies = ProfileComponent.class)
public interface ProfileOverviewComponent {
    void inject(ProfileOverviewFragment fragment);
    ProfileOverviewView getProfileOverviewView();
    ProfileOverviewView getProfileOverviewPresenter();
}
