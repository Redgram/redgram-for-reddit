package com.matie.redgram.data.managers.presenters.base;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.ui.base.BaseView;
import com.matie.redgram.ui.common.views.ContentView;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenterImpl implements BasePresenter {

    protected final ContentView view;
    protected final DatabaseManager databaseManager;
    private CompositeSubscription subscriptions;

    public BasePresenterImpl(ContentView contentView, DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.view = contentView;
    }

    protected <T> LifecycleTransformer<T> getTransformer() {
        final BaseView baseView = view.getBaseInstance();

        if (baseView instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) baseView).bindToLifecycle();
        } else if (baseView instanceof RxFragment) {
            return ((RxFragment) baseView).bindToLifecycle();
        }

        return null;
    }

    @Override
    public DatabaseManager databaseManager() {
        return databaseManager;
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
