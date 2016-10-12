package com.matie.redgram.data.managers.presenters;

import android.util.Log;

import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.user.views.UserListControllerView;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by matie on 2016-09-15.
 */
public class UserListPresenterImpl implements UserListPresenter {

    private UserListControllerView userListView;
    private final BaseContextView contextView;
    private final ContentView contentView;
    private final Realm realm;
    private Session session;
    private App app;
    private DatabaseManager databaseManager;

    private CompositeSubscription subscriptions;

    @Inject
    public UserListPresenterImpl(UserListControllerView userListView, ContentView contentView, App app) {
        this.userListView = userListView;
        this.contentView = contentView;
        this.contextView = contentView.getContentContext();
        this.app = app;
        this.databaseManager = app.getDatabaseManager();

        BaseActivity activity = contextView.getBaseActivity();
        this.realm = activity.getRealm();
        this.session = activity.getSession();
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
        Observable<List<UserItem>> userListObservable = getUserListObservable(realm, session.getUser());


        if(contextView instanceof BaseActivity){
            userListObservable = userListObservable.compose(((BaseActivity)contextView).bindToLifecycle());
        }else if(contextView instanceof BaseFragment){
            userListObservable = userListObservable.compose(((BaseFragment)contextView).bindToLifecycle());
        }

        Subscription userListSubscription =
                userListObservable
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<UserItem>>() {
                        @Override
                        public void onCompleted() {
                            Log.d("Error Getting User List", "User List Completed");
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
                        userItems.add(buildUserItem(user, sessionUser));
                    }

                    return userItems;
                });
    }

    private UserItem buildUserItem(User user, User sessionUser) {
        UserItem userItem = new UserItem(user.getId(), user.getUserName());
        if(sessionUser != null && user.getId().equalsIgnoreCase(sessionUser.getId())){
            userItem.setSelected(true);
        }
        return userItem;
    }


    @Override
    public void removeUser(String id, int position) {

    }

    @Override
    public void selectUser(String id, int position) {
        Observable<User> userObservable = DatabaseHelper.getUserByIdAsync(realm, id)
                .filter(RealmObject::isLoaded)
                .filter(user -> user != null)
                .flatMap(this::updateSessionWithSelectedUser);

        if(contextView instanceof BaseActivity){
            userObservable.compose(((BaseActivity)contextView).bindToLifecycle());
        }else if(contextView instanceof BaseFragment){
            userObservable.compose(((BaseFragment)contextView).bindToLifecycle());
        }

        Subscription selectUserSubscription = userObservable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<User>() {
                            @Override
                            public void onCompleted() {
                                //session updated - restart
                                //NOT CALLED - update realm?
                                userListView.restartContext();
                            }

                            @Override
                            public void onError(Throwable e) {
                                userListView.showErrorMessage("Could Not Select User");
                                Log.d("Select User", "Could Not Select User");
                            }

                            @Override
                            public void onNext(User user) {
                                //// TODO: 2016-09-29 restartContext should be moved to onCompleted
                                Log.d("Select User - onNext", user.getUserName());
                                userListView.restartContext();
                            }
                        });

        if(!getSubscriptions().isUnsubscribed()){
            subscriptions.add(selectUserSubscription);
        }
    }

    private Observable<User> updateSessionWithSelectedUser(User user) {
        realm.executeTransaction(realmInstance -> {
            if(session != null){
                //this should trigger the session listener set in the BaseActivity
                //UPDATE - listener was removed since it doesn't listen to specific changes in the session
                session.setUser(user);
                databaseManager.setCurrentToken(user.getTokenInfo().getToken());
            }
        });
        return user.asObservable();
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
