package com.matie.redgram.data.managers.presenters;

import android.util.Log;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthProvider;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.user.views.UserListControllerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserListPresenterImpl extends BasePresenterImpl implements UserListPresenter {

    private final Realm realm;
    private final RedditAuthInterface redditAuthClient;
    private UserListControllerView userListView;
    private Session session;
    private boolean enableDefault;

    @Inject
    public UserListPresenterImpl(UserListControllerView userListView,
                                 ContentView contentView,
                                 DatabaseManager databaseManager,
                                 RedditAuthInterface redditAuthClient,
                                 boolean enableDefault) {
        super(contentView, databaseManager);

        this.userListView = userListView;
        this.enableDefault = enableDefault;

        this.redditAuthClient = redditAuthClient;

        this.realm = databaseManager().getInstance();
        this.session = DatabaseHelper.getSession(realm);
    }

    @Override
    public void unregisterForEvents() {
        super.unregisterForEvents();

        DatabaseHelper.close(realm);
    }

    @Override
    public void getUsers() {
        Observable<List<UserItem>> userListObservable = getUserListObservable();

        Subscription userListSubscription =
                userListObservable
                    .compose(getTransformer())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<UserItem>>() {
                        @Override
                        public void onCompleted() {
                            Log.d("User List", "User List Completed");
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

        addSubscription(userListSubscription);
    }

    private Observable<List<UserItem>> getUserListObservable() {
        return DatabaseHelper.getUsersAsync(realm)
                .filter(RealmResults::isLoaded)
                .map(list -> {
                    List<UserItem> userItems = new ArrayList<>();

                    for(User user : list) {
                        if(session != null &&
                                (enableDefault && User.USER_GUEST.equalsIgnoreCase(user.getUserType())) ||
                                    !User.USER_GUEST.equalsIgnoreCase(user.getUserType())
                        ){
                            userItems.add(buildUserItem(user, session.getUser()));
                        }
                    }

                    return userItems;
                });
    }

    private UserItem buildUserItem(User user, User sessionUser) {
        UserItem userItem = new UserItem(user.getId(), user.getUserName());

        if (sessionUser != null) {
            if (user.getId().equalsIgnoreCase(sessionUser.getId())) {
                userItem.setSelected(true);
            }

            if (User.USER_GUEST.equalsIgnoreCase(user.getUserType())) {
                userItem.setDefault(true);
            }
        }

        return userItem;
    }

    // TODO: 2016-10-24 change to asynchronous
    @Override
    public void removeUser(String id, int position) {
        try {
            DatabaseHelper.deleteUserById(realm, id, null);
            if (userListView.getItem(position).isSelected()) {
                selectUser("Guest", 0);
            } else {
                userListView.removeItem(position);
            }
        } catch (IllegalStateException e) {
            Log.d("Remove User", e.getMessage());
            userListView.showErrorMessage(e.getMessage());
        }

    }

    @Override
    public void selectUser(String id, int position) {
        Observable<User> userObservable = DatabaseHelper.getUserByIdAsync(realm, id)
                .filter(RealmObject::isLoaded)
                .filter(user -> user != null)
                .flatMap(this::updateSessionWithSelectedUser);

        Subscription selectUserSubscription = userObservable
            .compose(getTransformer())
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

        addSubscription(selectUserSubscription);
    }

    @Override
    public void switchUser() {
       getDefaultRevokeAccessObservable()
               .compose(getTransformer())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribe(new SelectedUserSubscriber("Guest", 0));
    }

    @Override
    public void switchUser(String id, int position) {
        getDefaultRevokeAccessObservable()
                .compose(getTransformer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SelectedUserSubscriber(id, position));
    }

    private Observable<AccessToken> getDefaultRevokeAccessObservable() {
        return redditAuthClient
                .revokeToken(RedditAuthProvider.ACCESS_TOKEN);
    }

    private Observable<AccessToken> getRevokeTokenObservable(String token, String type) {
        return redditAuthClient.revokeToken(token, type);
    }


    private Observable<User> updateSessionWithSelectedUser(User user) {
        realm.executeTransaction(realmInstance -> {
            if(session != null){
                //this should trigger the session listener set in the BaseActivity
                //UPDATE - listener was removed since it doesn't listen to specific changes in the session
                session.setUser(user);
                databaseManager.setCurrentToken(realmInstance.copyFromRealm(user.getTokenInfo()));
            }
        });
        return user.asObservable();
    }

    private class SelectedUserSubscriber extends Subscriber<Object> {

        private String id;
        private int position;

        private SelectedUserSubscriber(String id, int position) {
            this.id = id;
            this.position = position;
        }

        @Override
        public void onCompleted() {
            selectUser(id, position);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Object accessToken) {

        }
    }
}
