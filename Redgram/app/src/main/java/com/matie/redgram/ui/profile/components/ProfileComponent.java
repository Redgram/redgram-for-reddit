package com.matie.redgram.ui.profile.components;

import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.profile.ProfileActivity;
import com.matie.redgram.ui.profile.modules.ProfileModule;
import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.user.UserComponent;

import dagger.Component;

@ActivityScope
@Component(dependencies = UserComponent.class, modules = ProfileModule.class)
public interface ProfileComponent extends UserComponent {
    void inject(ProfileActivity activity);

    ProfileActivity activity();
    DialogUtil getDialogUtil();
}
