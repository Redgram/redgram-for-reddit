package com.matie.redgram.ui.common.main;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import dagger.Component;

/**
 * Created by matie on 06/06/15.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                MainModule.class
        }
)
public interface MainComponent extends AppComponent{
    void inject(MainActivity activity);

    MainActivity activity();
    DialogUtil getDialogUtil();
}
