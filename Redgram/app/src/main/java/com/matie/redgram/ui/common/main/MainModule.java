package com.matie.redgram.ui.common.main;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 06/06/15.
 */
@Module
public class MainModule {

    private final MainActivity activity;

    public MainModule(MainActivity activity) {
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    MainActivity activity(){
        return activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }

}
