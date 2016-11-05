package com.matie.redgram.ui.common.auth;

import com.matie.redgram.data.managers.presenters.AuthPresenter;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.auth.views.AuthView;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import dagger.Component;

/**
 * Created by matie on 2016-02-21.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                AuthModule.class,
        }
)
public interface AuthComponent extends AppComponent {
    void inject(AuthActivity authActivity);
    AuthActivity activity();
    DialogUtil getDialogUtil();
    AuthView getAuthView();
    AuthPresenter getAuthPresenter();
}
