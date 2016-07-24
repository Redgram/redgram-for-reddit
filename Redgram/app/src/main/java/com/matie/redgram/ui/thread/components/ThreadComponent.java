package com.matie.redgram.ui.thread.components;

import com.matie.redgram.data.managers.presenters.ThreadPresenter;
import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.thread.modules.ThreadModule;
import com.matie.redgram.ui.thread.views.ThreadActivity;
import com.matie.redgram.ui.thread.views.ThreadView;

import dagger.Component;

/**
 * Created by matie on 2016-02-10.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = {
                ThreadModule.class
        }
    )
public interface ThreadComponent extends AppComponent {
    void inject(ThreadActivity activity);
    ThreadActivity activity();
    ThreadView getThreadView();
    ThreadPresenter getThreadPresenter();
    DialogUtil getDialogUtil();
}
