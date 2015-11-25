package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainActivity;

import dagger.Component;

/**
 * Created by matie on 2015-11-24.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                SubscriptionModule.class
        }
)
public interface SubscriptionComponent {
    void inject(SubscriptionActivity activity);
}
