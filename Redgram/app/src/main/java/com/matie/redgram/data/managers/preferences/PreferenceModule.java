package com.matie.redgram.data.managers.preferences;

import com.matie.redgram.ui.App;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 20/09/15.
 *
 * The module responsible for providing the preferences manager to application
 */
@Module
public class PreferenceModule {
    @Provides
    public PreferenceManager getPreferenceManager(App app){
        return new PreferenceManager(app.getApplicationContext());
    }
}
