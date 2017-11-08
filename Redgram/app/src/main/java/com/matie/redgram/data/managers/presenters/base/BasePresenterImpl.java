package com.matie.redgram.data.managers.presenters.base;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.views.BaseView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.RxActivity;
import com.trello.rxlifecycle.components.RxFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenterImpl implements BasePresenter {

    protected final App app;
    protected final BaseView view;
    private CompositeSubscription subscriptions;

    public BasePresenterImpl(BaseView baseView, App app) {
        this.app = app;
        this.view = baseView;
    }

    protected <T> LifecycleTransformer<T> getTransformer() {
        if (view instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) view).bindToLifecycle();
        } else if (view instanceof RxFragment) {
            return ((RxFragment) view).bindToLifecycle();
        }

        return null;
    }

    @Override
    public DatabaseManager databaseManager() {
        return app.getDatabaseManager();
    }

    @Override
    public void registerForEvents() {
        if (subscriptions == null) {
            subscriptions = new CompositeSubscription();
        }
    }

    @Override
    public void unregisterForEvents() {
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }

    protected void addSubscription(Subscription subscription) {
        if (subscription == null || subscriptions == null) return;
        subscriptions.add(subscription);
    }

    protected void removeSubscription(Subscription subscription) {
        if (subscription == null || subscriptions == null) return;
        subscriptions.remove(subscription);
    }
}
