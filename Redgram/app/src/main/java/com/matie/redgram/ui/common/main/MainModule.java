package com.matie.redgram.ui.common.main;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.common.utils.DialogUtil;

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

    @Provides MainActivity activity(){
        return (MainActivity)activity;
    }

    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }
}
