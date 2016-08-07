package com.matie.redgram.ui.profile.components;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.ProfileActivity;
import com.matie.redgram.ui.profile.modules.ProfileModule;

import dagger.Component;

/**
 * Created by matie on 2016-07-21.
 */
@ActivityScope
@Component(modules = ProfileModule.class,
            dependencies = AppComponent.class)
public interface ProfileComponent extends AppComponent {
    void inject(ProfileActivity activity);
    ProfileActivity activity();
    DialogUtil getDialogUtil();
}
