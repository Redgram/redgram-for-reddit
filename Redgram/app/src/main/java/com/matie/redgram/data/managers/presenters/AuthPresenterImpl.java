package com.matie.redgram.data.managers.presenters;

import android.net.Uri;
import android.widget.Toast;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.auth.views.AuthView;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AuthPresenterImpl extends BasePresenterImpl implements AuthPresenter {

    private final AuthView authView;
    private final RedditClientInterface redditClient;
    private Realm realm;
    private String authCode = "";

    @Inject
    public AuthPresenterImpl(AuthView authView, App app) {
        super(authView, app);
        this.authView = (AuthView) view;
        this.redditClient = app.getRedditClient();
        this.realm = getRealmInstance();
    }

    private Realm getRealmInstance() {
        return databaseManager().getInstance();
    }

    public void addRealmChangeListener(RealmChangeListener realmChangeListener) {
        if (realm == null || realmChangeListener == null) return;
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    public void registerForEvents() {
        super.registerForEvents();
        if (realm == null || realm.isClosed()) {
            realm = getRealmInstance();
        }
    }

    @Override
    public void unregisterForEvents() {
        super.unregisterForEvents();
        realm.removeAllChangeListeners();
        DatabaseHelper.close(realm);
    }

    @Override
    public void getAccessToken(String url) {
        final String codeQuery = "code";
        Uri uri = Uri.parse(url);
        if (uri != null && uri.getQueryParameterNames().contains(codeQuery)) {
            String code = uri.getQueryParameter(codeQuery);
            if(!code.equalsIgnoreCase(authCode)){
                authCode = code;
                authView.showLoading();
                bindAuthSubscription();
            }
        }
    }

    @Override
    public void getAccessToken() {
        authView.showLoading();
        Subscription authSubscription = redditClient.getAuthWrapper()
                .compose(getTransformer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AuthWrapper>() {
                    @Override
                    public void onCompleted() {
                        app.getToastHandler().showToast("Guest User Updated", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onError(Throwable e) {
                        authView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AuthWrapper authWrapper) {
                        updateSession(authWrapper);
                    }
                });

        addSubscription(authSubscription);
    }

    private void bindAuthSubscription() {
        Subscription authSubscription = redditClient.getAuthWrapper(authCode)
                .compose(getTransformer())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AuthWrapper>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        authView.hideLoading();
                        authView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AuthWrapper wrapper) {
                        authView.showPreferencesOptions(wrapper);
                    }
                });

        addSubscription(authSubscription);
    }

    @Override
    public void updateSession(AuthWrapper wrapper) {
        Session session = DatabaseHelper.getSession(realm);
        // if session user already exists and is of type Guest, and wrapper is of type Guest
        if(session != null && session.getUser() != null
                && User.USER_GUEST.equalsIgnoreCase(session.getUser().getUserType())
                && User.USER_GUEST.equalsIgnoreCase(wrapper.getType())){
            DatabaseHelper.setTokenInfo(databaseManager().getInstance(), wrapper.getAccessToken());
        }else{ // otherwise it's a new user or a null session/user
            DatabaseHelper.setSession(realm, wrapper);
        }
        // important - this is used in the services to capture the token of the current user
        if(session != null && session.getUser() != null){
            Token token = realm.copyFromRealm(session.getUser().getTokenInfo());
            databaseManager().setCurrentToken(token);
        }
    }

    @Override
    public RealmResults<User> getExistingUsers() {
        if (realm == null || realm.isClosed()) return null;
        return DatabaseHelper.getUsers(realm);
    }

    @Override
    public Prefs getPrefsByUserId(String userId) {
        if (realm == null || userId == null) return null;
        return DatabaseHelper.getPrefsByUserId(realm, userId);
    }
}
