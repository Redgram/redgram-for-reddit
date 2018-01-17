package com.matie.redgram.data.network.api.reddit.auth;


import android.content.Context;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.network.api.reddit.base.RedditService;
import com.matie.redgram.data.network.api.reddit.interceptors.AuthenticatorListener;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditAuthenticator;
import com.matie.redgram.data.network.api.reddit.interceptors.RedditGeneralInterceptor;
import com.matie.redgram.data.network.connection.ConnectionManager;
import com.matie.redgram.ui.auth.AuthActivity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Interceptor;
import retrofit2.Call;
import rx.Observable;

public class RedditAuthClient extends RedditService
        implements RedditAuthInterface, AuthenticatorListener {

    public static final String REFRESH_HEADER_TAG = "redgram-refresh-header";

    private final DatabaseManager databaseManager;
    private final ConnectionManager connectionManager;
    private final RedditAuthProvider authProvider;
    private final String uuid;

    @Inject
    public RedditAuthClient(Context context,
                            DatabaseManager databaseManager,
                            ConnectionManager connectionManager) {
        super(context, databaseManager);

        this.databaseManager = databaseManager;
        this.connectionManager = connectionManager;

        this.authProvider = buildRetrofit(REDDIT_HOST_ABSOLUTE).create(RedditAuthProvider.class);
        this.uuid = UUID.randomUUID().toString();
    }

    @Override
    protected List<Authenticator> getAuthenticators() {
        return Collections.singletonList(new RedditAuthenticator(databaseManager, this, this));
    }

    @Override
    protected List<Interceptor> getInterceptors() {
        return Collections.singletonList(new RedditGeneralInterceptor(connectionManager, databaseManager, this));
    }

    @Override
    public Observable<AuthWrapper> getAuthWrapper(){
        return getAccessTokenObservable()
                .filter(accessToken -> accessToken.getAccessToken() != null) //make sure it's not null
                .map(accessToken -> {
                    AuthWrapper wrapper = new AuthWrapper();
                    wrapper.setAccessToken(accessToken);
                    wrapper.setType(User.USER_GUEST);
                    return wrapper;
                });
    }

    @Override
    public Observable<AccessToken> getAccessToken(String code) {
        return authProvider.obtainAccessToken(GRANT_TYPE_AUTHORIZE, code, REDIRECT_URI);
    }

    public Observable<AccessToken> getAccessTokenObservable() {
        return authProvider.obtainAccessToken(GRANT_TYPE_INSTALLED, uuid);
    }

    @Override
    public Call<AccessToken> getAccessToken() {
        return authProvider.obtainAccessTokenSync(GRANT_TYPE_INSTALLED, uuid);
    }

    @Override
    public Call<AccessToken> refreshToken() {
        return authProvider.refreshAccessToken(REFRESH_HEADER_TAG, GRANT_TYPE_REFRESH, getRefreshToken());
    }

    //revoke token of the current authenticated user
    @Override
    public Observable<AccessToken> revokeToken(String tokenType){
        if (RedditAuthProvider.ACCESS_TOKEN.equalsIgnoreCase(tokenType)) {
            return authProvider.revokeToken(getToken(), tokenType);
        } else if (RedditAuthProvider.REFRESH_TOKEN.equalsIgnoreCase(tokenType)) {
            return authProvider.revokeToken(getRefreshToken(), tokenType);
        }

        return null;
    }

    //revoke token passed to this method
    //returns code 204 even if the token was invalid
    @Override
    public Observable<AccessToken> revokeToken(String token, String tokenType) {
        if (RedditAuthProvider.ACCESS_TOKEN.equalsIgnoreCase(tokenType)
                || RedditAuthProvider.REFRESH_TOKEN.equalsIgnoreCase(tokenType)) {
            return authProvider.revokeToken(token, tokenType);
        }

        return null;
    }

    @Override
    public void onAuthenticationRequest() {
        context.startActivity(AuthActivity.intent(context, true));
    }
}
