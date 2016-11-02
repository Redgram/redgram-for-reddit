package com.matie.redgram.data.managers.presenters;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.auth.AuthActivity;
import com.matie.redgram.ui.common.auth.views.AuthView;
import com.matie.redgram.ui.common.base.BaseActivity;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Presenter implementation for the Authentication View.
 */
public class AuthPresenterImpl implements AuthPresenter {

    private final App app;
    private final AuthView authView;
    private final RedditClientInterface redditClient;
    private Subscription authSubscription;
    private CompositeSubscription subscriptions;
    private String authCode = "";

    @Inject
    public AuthPresenterImpl(AuthView authView, App app) {
        this.app = app;
        this.authView = authView;
        this.redditClient = app.getRedditClient();
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
        if(subscriptions != null && subscriptions.hasSubscriptions()){
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

    @Override
    public void getAccessToken() {
        authView.showLoading();
        authSubscription = redditClient.getAuthWrapper()
                .compose(((AuthActivity)authView.getContentContext()).bindToLifecycle())
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
    }

    private void bindAuthSubscription() {
        authSubscription = redditClient.getAuthWrapper(authCode)
                .compose(((AuthActivity)authView.getContentContext()).bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AuthWrapper>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e("getAccessToken", e.toString());
                        authView.hideLoading();
                        authView.showErrorMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AuthWrapper wrapper) {
                        authView.showPreferencesOptions(wrapper);
                    }
                });
    }

    @Override
    public void updateSession(AuthWrapper wrapper) {
        BaseActivity baseActivity = authView.getContentContext().getBaseActivity();
        Session session = baseActivity.getSession();
        //if session user already exists and is of type Guest, and wrapper is of type Guest
        if(session != null && session.getUser() != null
                && User.USER_GUEST.equalsIgnoreCase(session.getUser().getUserType())
                && User.USER_GUEST.equalsIgnoreCase(wrapper.getType())){
            DatabaseHelper.setTokenInfo(baseActivity.getRealm(), wrapper.getAccessToken());
        }else{ //otherwise it's a new user or a null session/user
            DatabaseHelper.setSession(baseActivity.getRealm(), wrapper);
        }
        //important - this is used in the services to capture the token of the current user
        if(session != null && session.getUser() != null){
            Token token = baseActivity.getRealm().copyFromRealm(session.getUser().getTokenInfo());
            app.getDatabaseManager().setCurrentToken(token);
        }
    }
}
