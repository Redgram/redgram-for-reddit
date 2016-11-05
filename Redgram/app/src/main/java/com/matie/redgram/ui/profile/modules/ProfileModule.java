package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.ProfileActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by matie on 2016-07-21.
 */
@Module
public class ProfileModule {

    private ProfileActivity activity;

    public ProfileModule(ProfileActivity activity){
        this.activity = activity;
    }

    @ActivityScope
    @Provides
    ProfileActivity activity(){
        return activity;
    }

    @ActivityScope
    @Provides
    DialogUtil provideDialogUtil(){
        return new DialogUtil(activity);
    }
}
