package com.matie.redgram.ui.profile;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

/**
 * Created by matie on 2016-07-21.
 */
@ActivityScope
@Component(modules = ProfileModule.class,
            dependencies = AppComponent.class)
public interface ProfileComponent extends AppComponent {
    void inject(ProfileActivity activity);
}
