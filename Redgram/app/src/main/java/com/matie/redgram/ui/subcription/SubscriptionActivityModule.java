package com.matie.redgram.ui.subcription;

import android.app.Activity;

import com.matie.redgram.ui.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2015-11-29.
 */
@Module
public class SubscriptionActivityModule {
    private final Activity activity;

    public SubscriptionActivityModule(Activity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    SubscriptionActivity activity(){
        return (SubscriptionActivity)activity;
    }

}
