package com.matie.redgram.ui.user;

import com.matie.redgram.ui.scopes.UserScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides @UserScope
    public UserComponentInjector providesInjector(UserComponent userComponent) {
        return new UserComponentInjector(userComponent);
    }
}
