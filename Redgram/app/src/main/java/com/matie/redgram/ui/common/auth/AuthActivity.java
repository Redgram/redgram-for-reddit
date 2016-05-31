package com.matie.redgram.ui.common.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.AuthPresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.auth.views.AuthView;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AuthActivity extends BaseActivity implements AuthView {

    @InjectView(R.id.web_view) WebView mContentView;
    @InjectView(R.id.loading_view) FrameLayout loadingLayout;

    private AuthComponent authComponent;

    @Inject App app;
    @Inject DatabaseManager databaseManager;
    @Inject AuthPresenterImpl authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setUpWebView();
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        authPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        authPresenter.unregisterForEvents();
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
}
