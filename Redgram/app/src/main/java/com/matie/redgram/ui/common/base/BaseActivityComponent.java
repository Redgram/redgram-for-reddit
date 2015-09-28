package com.matie.redgram.ui.common.base;

import com.matie.redgram.ui.ActivityScope;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainModule;
import com.matie.redgram.ui.common.utils.DialogUtil;

import dagger.Component;

/**
 * Created by matie on 09/06/15.
 */
@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = BaseActivityModule.class
)
public interface BaseActivityComponent extends AppComponent {
        void inject(BaseActivity activity);

        BaseActivity activity();
        DialogUtil getDialogUtil();
}
