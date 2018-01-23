package com.matie.redgram.ui.main;

import com.matie.redgram.data.managers.presenters.MainPresenter;
import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.userlist.UserListComponent;
import com.matie.redgram.ui.userlist.UserListModule;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                MainModule.class,
                UserListModule.class
        }
)
public interface MainComponent extends AppComponent {
    void inject(MainActivity activity);

    MainActivity activity();
    MainPresenter getMainPresenter();
    DialogUtil getDialogUtil();
    UserListComponent getUserListComponent(UserListModule module);
}
