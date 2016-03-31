package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.FragmentScope;

import dagger.Component;

/**
 * Created by matie on 2015-11-24.
 */
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
