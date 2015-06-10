package com.matie.redgram.ui.common;

import android.app.Activity;

import com.matie.redgram.ui.ActivityScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class MainModule {
    private final Activity activity;

    public MainModule(Activity activity) {
        this.activity = activity;
    }

    @Provides @ActivityScope Activity activity(){
        return activity;
    }
}
