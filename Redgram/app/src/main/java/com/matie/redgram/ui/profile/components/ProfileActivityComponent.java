package com.matie.redgram.ui.profile.components;

import com.matie.redgram.data.managers.presenters.ProfileActivityPresenter;
import com.matie.redgram.ui.profile.ProfileActivityFragment;
import com.matie.redgram.ui.profile.modules.ProfileActivityModule;
import com.matie.redgram.ui.profile.views.ProfileActivityView;
import com.matie.redgram.ui.scopes.ProfileScope;

import dagger.Component;

@ProfileScope
@Component(
        dependencies = ProfileComponent.class,
        modules = {
                ProfileActivityModule.class
        }
)
public interface ProfileActivityComponent {
    void inject(ProfileActivityFragment profileActivityFragment);

    ProfileActivityView getProfileActivityView();
    ProfileActivityPresenter getProfileActivityPresenter();
}
