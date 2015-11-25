package com.matie.redgram.ui.common.main;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.common.utils.StringUtils;

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

    @ActivityScope
    @Provides
    MainActivity activity(){
        return (MainActivity)activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

}
