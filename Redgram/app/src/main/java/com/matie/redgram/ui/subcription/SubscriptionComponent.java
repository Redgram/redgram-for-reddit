package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.scopes.SubscriptionScope;

import dagger.Component;

@SubscriptionScope
@Component(
        dependencies = SubscriptionActivityComponent.class,
        modules = {
                SubscriptionModule.class
        }
)
public interface SubscriptionComponent {
    void inject(SubscriptionFragment subscriptionFragment);
}
