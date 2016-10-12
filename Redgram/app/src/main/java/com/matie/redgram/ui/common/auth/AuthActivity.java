package com.matie.redgram.ui.common.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.BaseContextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AuthActivity extends BaseActivity implements AuthView {

    public static final String RESULT_USER_NAME = "result_user_name";
    public static final String RESULT_USER_ID = "result_user_id";
    public static final String REQUEST_NEW_ACCOUNT = "request_new_account";
    public static final String REQUEST_APP_OAUTH = "request_app_oauth";

    @InjectView(R.id.web_view)
    WebView mContentView;
    @InjectView(R.id.loading_view)
    FrameLayout loadingLayout;

    private AuthComponent authComponent;
    private RealmChangeListener realmChangeListener;
    private RealmResults<User> users;

    private boolean resultIncluded;
    private boolean requestAppOAuth;
    private String userId;
    private String userName;

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
        checkIntent(getIntent());
        setUpRealm();
        if(!requestAppOAuth){
            setUpWebView();
        }else{
            authPresenter.getAccessToken();
        }
    }

    private void checkIntent(Intent intent) {
        //check whether result expected or not
        resultIncluded = intent.getBooleanExtra(REQUEST_NEW_ACCOUNT, false);
        //check if it's requesting an app oauth
        requestAppOAuth = intent.getBooleanExtra(REQUEST_APP_OAUTH, false);
    }

    private void setUpRealm() {
        realmChangeListener = () -> {
            //changes made in this context are related to the session being set
            transitionToMainActivity(resultIncluded, true);
        };
        getRealm().addChangeListener(realmChangeListener);
    }

    private void setResult(boolean isSuccess) {
        Intent resultIntent = new Intent();
        if(userId != null && userName != null){
            resultIntent.putExtra(RESULT_USER_NAME, userName);
            resultIntent.putExtra(RESULT_USER_ID, userId);
        }
        setResult(isSuccess ? RESULT_OK : RESULT_CANCELED, resultIntent);
    }

    private void setUpWebView() {
        clearCookies(CookieManager.getInstance());
        mContentView.getSettings().setBuiltInZoomControls(true);
        mContentView.getSettings().setDisplayZoomControls(false);
        mContentView.getSettings().setJavaScriptEnabled(true);
        mContentView.loadUrl(getAuthUrl());
        mContentView.setWebViewClient(new WebViewCustomClient());
    }

    private void clearCookies(CookieManager manager){
        if(manager.getCookie(getAuthUrl()) != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                manager.removeSessionCookies(value -> {});
            }else{
                manager.removeSessionCookie();
            }
        }
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
    protected RealmChangeListener getRealmSessionChangeListener() {
        return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        authPresenter.registerForEvents();
        getRealm().addChangeListener(realmChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        authPresenter.unregisterForEvents();
        getRealm().removeAllChangeListeners();
    }

    @Override
    public void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        mContentView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mContentView.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage() {
        mContentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String error) {
        mContentView.setVisibility(View.VISIBLE);
        if(resultIncluded){
            transitionToMainActivity(resultIncluded, false);
        }else{
            app.getToastHandler().showToast(error, Toast.LENGTH_LONG);
        }
    }

    @Override
    public BaseContextView getContentContext() {
        return getBaseActivity();
    }

    @Override
    public void transitionToMainActivity(boolean resultIncluded, boolean isSuccess) {
        if(resultIncluded){
            setResult(isSuccess);
            finish();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private static String getAuthUrl() {
        return RedditServiceBase.REDDIT_HOST_ABSOLUTE + RedditServiceBase.NAMESPACE + RedditServiceBase.OAUTH_URL
                .replace("{client_id}", RedditServiceBase.API_KEY)
                .replace("{redirect_uri}", RedditServiceBase.REDIRECT_URI)
                .replace("{duration}", RedditServiceBase.DURATION)
                .replace("{scope}", RedditServiceBase.scopeList);
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
        if(getRealm() != null && !getRealm().isClosed()){
            users = DatabaseHelper.getUsers(getRealm());
            List<String> userNames = new ArrayList<>();
            for(User user : users){
                if(wrapper.getAuthUser().getId().equalsIgnoreCase(user.getId())){
                    showUserExistsDialog();
                    return;
                }else{
                    userNames.add(user.getUserName());
                }
            }

            //add the results after making sure the new user doesn't exist
            userId = wrapper.getAuthUser().getId();
            userName = wrapper.getAuthUser().getName();

            //preferences
            if(!userNames.isEmpty()){
                showExistingPreferencesDialog(userNames, wrapper);
            }else{
                showPreferencesSyncDialog(wrapper);
            }

        }
    }

    private void showUserExistsDialog() {
        MaterialDialog.Builder builder = dialogUtil.build()
                .title("Account Exists")
                .positiveText("Login Again")
                .onPositive((dialog, which) -> {
                     //open web page again
                    clearCookies(CookieManager.getInstance());
                    mContentView.loadUrl(getAuthUrl());
                    hideLoading();
                });
        if(resultIncluded){
            builder.negativeText("Cancel")
                .onNegative((dialog, which) -> {
                    //close with no result - userId and userName are still null
                    setResult(true);
                    finish();
                });
        }

        builder.cancelable(false)
                .canceledOnTouchOutside(false)
                .show();

    }

    private void showExistingPreferencesDialog(List<String> userNames, AuthWrapper wrapper) {
        dialogUtil.build()
                .title("Would you like to use settings from an existing account?")
                .items(userNames)
                .itemsCallback((dialog, itemView, which, text) -> {
                    for(User user : users){
                        if(user.getUserName().equalsIgnoreCase(text.toString())){
                            Prefs prefs = DatabaseHelper.getPrefsByUserId(getRealm(), user.getId());

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
                    authPresenter.updateSession(wrapper);
                })
                .negativeText("No, thanks")
                .onNegative((dialog, which) -> showPreferencesSyncDialog(wrapper))
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }

    private void showPreferencesSyncDialog(AuthWrapper wrapper) {
        dialogUtil.build()
                .title("Use preferences from your Reddit account?")
                .positiveText("Yes")
                .negativeText("No")
                .onPositive((dialog, which) -> DatabaseHelper.setSession(getRealm(), wrapper))
                .onNegative((dialog, which) -> {
                    wrapper.getAuthPrefs().setToDefault();
                    authPresenter.updateSession(wrapper);
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();
    }


    public static Intent intent(Context context, boolean isLauncher){
        Intent intent = new Intent(context, AuthActivity.class);
        //common flag
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //only if true
        if(isLauncher){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(REQUEST_APP_OAUTH, true);
        }else{
            intent.putExtra(REQUEST_NEW_ACCOUNT, true);
        }
        return intent;
    }
}
