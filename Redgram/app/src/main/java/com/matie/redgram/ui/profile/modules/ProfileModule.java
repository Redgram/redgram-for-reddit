package com.matie.redgram.ui.profile.modules;

import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.ProfileActivity;

import dagger.Module;
import dagger.Provides;

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
