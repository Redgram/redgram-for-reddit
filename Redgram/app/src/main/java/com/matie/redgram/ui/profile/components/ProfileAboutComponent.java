package com.matie.redgram.ui.profile.components;

import com.matie.redgram.data.managers.presenters.ProfileAboutPresenter;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.profile.ProfileAboutFragment;
import com.matie.redgram.ui.profile.modules.ProfileAboutModule;
import com.matie.redgram.ui.profile.views.ProfileAboutView;

import dagger.Component;

/**
 * Created by matie on 2017-09-30.
 */
@FragmentScope
@Component(
        dependencies = ProfileComponent.class,
        modules = {
                ProfileAboutModule.class
        }
)
public interface ProfileAboutComponent {
    void inject(ProfileAboutFragment profileAboutFragment);

    ProfileAboutView getProfileAboutView();
    ProfileAboutPresenter getProfileAboutPresenter();
}
