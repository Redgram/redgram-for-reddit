package com.matie.redgram.ui.settings;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

/**
 * Created by matie on 2016-06-06.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                SettingsModule.class
        }
)
public interface SettingsComponent extends AppComponent {
    void inject(SettingsActivity activity);
}
