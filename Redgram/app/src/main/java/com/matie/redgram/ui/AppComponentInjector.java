package com.matie.redgram.ui;


import android.support.annotation.NonNull;

import com.matie.redgram.ui.auth.AuthActivity;
import com.matie.redgram.ui.base.BaseActivity;
import com.matie.redgram.ui.user.DaggerUserComponent;
import com.matie.redgram.ui.user.UserComponent;
import com.matie.redgram.ui.user.UserModule;

import javax.inject.Inject;

public class AppComponentInjector {

    private AppComponent appComponent;
    private UserComponent userComponent;

    @Inject
    public AppComponentInjector(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    private UserComponent getUserComponent() {
        if (userComponent == null) {
            userComponent = DaggerUserComponent.builder()
                    .appComponent(appComponent)
                    .userModule(new UserModule())
                    .build();
        }

        return userComponent;
    }

    void clearUserComponent() {
        userComponent = null;
    }

    AppComponent getParentComponent(@NonNull BaseActivity activity) {
        if (activity instanceof AuthActivity) {
            return appComponent;
        } else {
            return getUserComponent();
        }
    }
}
