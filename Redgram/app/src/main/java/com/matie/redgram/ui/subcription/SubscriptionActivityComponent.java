package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.user.UserComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = UserComponent.class,
        modules = {
                SubscriptionActivityModule.class
        }
)
public interface SubscriptionActivityComponent extends UserComponent {
    void inject(SubscriptionActivity activity);
}
