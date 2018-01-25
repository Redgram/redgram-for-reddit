package com.matie.redgram.ui.settings;

import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.user.UserComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = UserComponent.class,
        modules = {
                SettingsModule.class
        }
)
public interface SettingsComponent extends UserComponent {
    void inject(SettingsActivity activity);
}
