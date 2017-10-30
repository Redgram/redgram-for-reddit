package com.matie.redgram.data.managers.presenters.base;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.BaseView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.RxFragment;

/**
 * Created by matie on 2017-10-29.
 */

public class BasePresenterImpl implements BasePresenter {

    protected final App app;
    protected final BaseView view;

    public BasePresenterImpl(BaseView baseView, App app) {
        this.app = app;
        this.view = baseView;
    }

    protected <T> LifecycleTransformer<T> getTransformer() {
        if (view instanceof RxActivity) {
            return ((RxActivity) view).bindToLifecycle();
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).bindToLifecycle();
        }

        return null;
    }

    @Override
    public void registerForEvents() {

    }

    @Override
    public void unregisterForEvents() {

    }
}
