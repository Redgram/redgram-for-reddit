package com.matie.redgram.ui.common.base;

import android.app.Activity;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.DialogUtil;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 27/09/15.
 */
@Module
public class BaseActivityModule {
    private final Activity activity;

    public BaseActivityModule(Activity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    BaseActivity activity(){
        return (BaseActivity)activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }
}
