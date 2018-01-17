package com.matie.redgram.ui.subcription;

import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

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
