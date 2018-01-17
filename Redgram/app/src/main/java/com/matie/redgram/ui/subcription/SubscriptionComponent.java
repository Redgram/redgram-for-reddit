package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.scopes.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(
        dependencies = SubscriptionActivityComponent.class,
        modules = {
                SubscriptionModule.class
        }
)
public interface SubscriptionComponent {
    void inject(SubscriptionFragment subscriptionFragment);
}
