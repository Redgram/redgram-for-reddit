package com.matie.redgram.ui.common.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.AuthPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseHelper;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.auth.views.AuthView;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AuthActivity extends BaseActivity implements AuthView {

    @InjectView(R.id.web_view)
    WebView mContentView;
    @InjectView(R.id.loading_view)
    FrameLayout loadingLayout;

    private AuthComponent authComponent;
    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private RealmResults<User> users;

    @Inject
    App app;
    @Inject
    DatabaseManager databaseManager;
    @Inject
    AuthPresenterImpl authPresenter;
    @Inject
    DialogUtil dialogUtil;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setUpWebView();
        setUpRealm();
    }

    private void setUpRealm() {
        realm = app.getDatabaseManager().getInstance();

        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                //changes made in this context are related to the session being set
                app.setupUserPrefs();
                transitionToMainActivity();
            }
        };

        realm.addChangeListener(realmChangeListener);
    }

    private void setUpWebView() {
        String url = RedditServiceBase.REDDIT_HOST_ABSOLUTE + RedditServiceBase.NAMESPACE + RedditServiceBase.OAUTH_URL
                .replace("{client_id}", RedditServiceBase.API_KEY)
                .replace("{redirect_uri}", RedditServiceBase.REDIRECT_URI)
                .replace("{duration}", RedditServiceBase.DURATION)
                .replace("{scope}", RedditServiceBase.scopeList);

        mContentView.getSettings().setBuiltInZoomControls(true);
        mContentView.getSettings().setDisplayZoomControls(false);
        mContentView.getSettings().setJavaScriptEnabled(true);
        mContentView.loadUrl(url);
        mContentView.setWebViewClient(new WebViewCustomClient());
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        authComponent = DaggerAuthComponent.builder()
                        .appComponent(appComponent)
                        .authModule(new AuthModule(this))
                        .build();
        authComponent.inject(this);
    }

    @Override
    public AppComponent component() {
        return authComponent;
    }

    @Override
    protected BaseActivity activity() {
        return getBaseActivity();
    }

    @Override
    public App app() {
        return app;
    }

    @Override
    public DialogUtil getDialogUtil() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auth;
    }

    @Override
    protected int getContainerId() {
        return 0;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        DatabaseHelper.close(realm);
    }

    @Override
    public void onResume() {
        super.onResume();
        authPresenter.registerForEvents();
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        authPresenter.unregisterForEvents();
        realm.removeAllChangeListeners();
    }

    @Override
    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage() {
        mContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String error) {
        mContentView.setVisibility(View.VISIBLE);
        app.getToastHandler().showToast(error, Toast.LENGTH_LONG);
    }

    @Override
    public void transitionToMainActivity() {
        //go to MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public BaseActivity getBaseActivity() {
        return this;
    }

    @Override
    public BaseFragment getBaseFragment() {
        return null;
    }

    private class WebViewCustomClient extends WebViewClient {

        public WebViewCustomClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            authPresenter.getAccessToken(url);
        }
    }

    @Override
    public void showPreferencesOptions(AuthWrapper wrapper) {
        if(realm !=null && !realm.isClosed()){
            users = DatabaseHelper.getUsers(realm);
            List<String> userNames = new ArrayList<>();
            for(User user : users){
                userNames.add(user.getUserName());
            }

            if(!userNames.isEmpty()){
                showExistingPreferencesDialog(userNames, wrapper);
            }else{
                showPreferencesSyncDialog(wrapper);
            }

        }
    }

    private void showExistingPreferencesDialog(List<String> userNames, AuthWrapper wrapper) {
        dialogUtil.build()
                .title("Would you like to use settings from an existing account?")
                .items(userNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        for(User user : users){
                            if(user.getUserName().equalsIgnoreCase(text.toString())){
                                Prefs prefs = DatabaseHelper.getPrefsByUserId(realm, user.getId());

                                AuthPrefs authPrefs = new AuthPrefs();
                                authPrefs.setDefaultCommentSort(prefs.getDefaultCommentSort());
                                authPrefs.setMinCommentScore(prefs.getMinCommentsScore());
                                authPrefs.setMinLinkScore(prefs.getMinLinkScore());
                                authPrefs.setNumComments(prefs.getNumComments());
                                authPrefs.setNumsites(prefs.getNumSites());
                                authPrefs.setShowFlair(prefs.isShowFlair());
                                authPrefs.setShowLinkFlair(prefs.isShowLinkFlair());
                                authPrefs.setShowTrending(prefs.isShowTrending());
                                authPrefs.setStoreVisits(prefs.isStoreVisits());
                                authPrefs.setMedia(prefs.getMedia());
                                authPrefs.setHighlightControversial(prefs.isHighlightControversial());
                                authPrefs.setIgnoreSuggestedSort(prefs.isIgnoreSuggestedSort());

                                wrapper.setAuthPrefs(authPrefs);
                                break;
                            }
                        }
                        app.getDatabaseManager().setSession(wrapper);
                    }
                })
                .negativeText("No, thanks")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showPreferencesSyncDialog(wrapper);
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showPreferencesSyncDialog(AuthWrapper wrapper) {
        dialogUtil.build()
                .title("Use preferences from your Reddit account?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        app.getDatabaseManager().setSession(wrapper);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        wrapper.getAuthPrefs().setToDefault();
                        app.getDatabaseManager().setSession(wrapper);
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }
}
