package com.matie.redgram.ui.settings;

import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                SettingsModule.class
        }
)
public interface SettingsComponent {
    void inject(SettingsActivity activity);
}
