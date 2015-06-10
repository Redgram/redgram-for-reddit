package com.matie.redgram.ui.common;

import android.app.Activity;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;

import dagger.Component;

/**
 * Created by matie on 06/06/15.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = MainModule.class
)
public interface MainComponent extends BaseComponent {
    void inject(Activity activity);
    Activity activity();
}
