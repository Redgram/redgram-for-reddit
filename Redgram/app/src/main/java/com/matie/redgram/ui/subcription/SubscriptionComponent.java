package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.FragmentScope;
import com.matie.redgram.ui.common.main.MainActivity;

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
