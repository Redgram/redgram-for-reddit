package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

/**
 * Created by matie on 2015-11-29.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                SubscriptionActivityModule.class
        }
)
public interface SubscriptionActivityComponent extends AppComponent {
    void inject(SubscriptionActivity activity);
}
