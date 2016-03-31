package com.matie.redgram.data.managers.storage.preferences;

import com.matie.redgram.ui.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 20/09/15.
 *
 * The module responsible for providing the preferences manager to application
 */
@Module
public class PreferenceModule {
    @Singleton
    @Provides
    public PreferenceManager providePreferenceManager(App app){
        return new PreferenceManager(app.getApplicationContext());
    }
}
