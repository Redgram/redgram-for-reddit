package com.matie.redgram.ui.thread.components;

import com.matie.redgram.data.managers.presenters.ThreadPresenter;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.scopes.ActivityScope;
import com.matie.redgram.ui.thread.ThreadActivity;
import com.matie.redgram.ui.thread.modules.ThreadModule;
import com.matie.redgram.ui.thread.views.ThreadView;
import com.matie.redgram.ui.user.UserComponent;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = UserComponent.class,
        modules = {
                ThreadModule.class
        }
    )
public interface ThreadComponent extends UserComponent {
    void inject(ThreadActivity activity);

    ThreadActivity activity();
    ThreadView getThreadView();
    ThreadPresenter getThreadPresenter();
    DialogUtil getDialogUtil();
}
