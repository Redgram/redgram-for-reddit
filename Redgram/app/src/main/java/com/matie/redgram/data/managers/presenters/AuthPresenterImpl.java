package com.matie.redgram.data.managers.presenters;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.matie.redgram.data.managers.storage.db.session.SessionManager;
import com.matie.redgram.data.managers.storage.preferences.PreferenceManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.auth.views.AuthView;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;


/**
 * Created by matie on 2016-02-21.
 */
public class AuthPresenterImpl implements AuthPresenter {

    private final AuthView authView;
    private final RedditClient redditClient;
    private final SharedPreferences sp;
    private final SessionManager sessionManager;
    private Subscription authSubscription;
    private CompositeSubscription subscriptions;
    private String authCode = "";

    @Inject
    public AuthPresenterImpl(AuthView authView, App app) {
        this.authView = authView;
        this.redditClient = app.getRedditClient();
        this.sp = app.getPreferenceManager().getSharedPreferences(PreferenceManager.OAUTH_PREF);
        this.sessionManager = app.getSessionManager();
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(subscriptions.isUnsubscribed()){
            if(authSubscription != null){
                subscriptions.add(authSubscription);
            }
        }
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() || subscriptions != null){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getAccessToken(String url) {
        if (url.contains("?code=") || url.contains("&code=")) {
            Uri uri = Uri.parse(url);
            String code = uri.getQueryParameter("code");
            if(!authCode.equalsIgnoreCase(code)){
                authCode = code;
                authView.showLoading();
                bindAuthSubscription();
            }
        }
    }

    private void bindAuthSubscription() {
        authSubscription = (Subscription)bindActivity(authView.getBaseActivity(),
                redditClient.getAuthWrapper(authCode))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AuthWrapper>() {
                    @Override
                    public void onCompleted() {
                        authView.transitionToMainActivity();
                        authView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getAccessToken", e.toString());
                        authView.hideLoading();
                        authView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(AuthWrapper wrapper) {
                        sessionManager.setSession(wrapper);
                    }
                });
    }


}
