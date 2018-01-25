package com.matie.redgram.ui.main;

import com.matie.redgram.data.managers.presenters.MainPresenter;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.user.UserComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = UserComponent.class,
        modules = {
                MainModule.class,
        }
)
public interface MainComponent extends UserComponent {
    void inject(MainActivity activity);

    MainActivity activity();
    MainPresenter getMainPresenter();
    DialogUtil getDialogUtil();
}
