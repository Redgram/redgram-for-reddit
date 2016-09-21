package com.matie.redgram.data.managers.presenters;

import android.util.Log;

import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.user.UserListView;
import com.matie.redgram.ui.common.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by matie on 2016-09-15.
 */
public class UserListPresenterImpl implements UserListPresenter {

    private UserListControllerView userListView;
    private final BaseContextView baseContextView;
    private final Realm realm;

    private CompositeSubscription subscriptions;

    @Inject
    public UserListPresenterImpl(UserListControllerView userListView, BaseContextView contextView, App app) {
        this.userListView = userListView;
        this.baseContextView = contextView;
        this.realm = app.getDatabaseManager().getInstance();
    }

    public void setUserListView(UserListControllerView userListView){
        this.userListView = userListView;
    }

    @Override
    public void registerForEvents() {
        initializeSubscriptions();
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions != null && subscriptions.hasSubscriptions()){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getUsers() {
        Subscription userListSubscription =
                DatabaseHelper.getSessionUserAsync(realm)
                    .flatMap(user -> getUserListObservable(realm, user))
                    .compose(baseContextView.getBaseActivity().bindToLifecycle())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<UserItem>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("Error Getting User List", e.getMessage());
                        }

                        @Override
                        public void onNext(List<UserItem> userItems) {
                            userListView.populateView(userItems);
                        }
                    });
        if(!getSubscriptions().isUnsubscribed()){
            subscriptions.add(userListSubscription);
        }
    }

    private Observable<List<UserItem>> getUserListObservable(Realm realm, User sessionUser){
        return DatabaseHelper.getUsersAsync(realm)
                .map(list -> {
                    List<UserItem> userItems = new ArrayList<>();

                    for(User user : list){
                        UserItem userItem = new UserItem(user.getUserName());
                        if(sessionUser != null && user.getId().equalsIgnoreCase(sessionUser.getId())){
                            userItem.setSelected(true);
                        }
                        userItems.add(userItem);
                    }

                    return userItems;
                });
    }


    @Override
    public void addUser(String username) {

    }

    @Override
    public void removeUser(String username) {

    }

    @Override
    public void selectUser(String username) {

    }

    @Override
    public void closeConnection() {
        DatabaseHelper.close(realm);
    }

    private void initializeSubscriptions() {
        if(subscriptions == null){
            subscriptions = new CompositeSubscription();
        }
    }

    private CompositeSubscription getSubscriptions() {
        initializeSubscriptions();
        return subscriptions;
    }
}
