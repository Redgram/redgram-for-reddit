package com.matie.redgram.data.network.api.reddit.interceptors;


import android.util.Log;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Authenticator;
import okhttp3.Challenge;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

import static com.matie.redgram.data.network.api.reddit.auth.RedditAuthClient.REFRESH_HEADER_TAG;
import static com.matie.redgram.data.network.api.reddit.base.RedditServiceBase.BASIC;
import static com.matie.redgram.data.network.api.reddit.base.RedditServiceBase.BEARER;

public class RedditAuthenticator implements Authenticator {

    private final DatabaseManager databaseManager;
    private final RedditAuthInterface redditAuthClient;
    private final AuthenticatorListener listener;

    @Inject
    public RedditAuthenticator(DatabaseManager databaseManager,
                               RedditAuthInterface redditAuthClient,
                               AuthenticatorListener listener) {
        this.databaseManager = databaseManager;
        this.redditAuthClient = redditAuthClient;
        this.listener = listener;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if ((response.code() != 401 && response.code() != 403) || response.message() == null) {
            return null;
        }

        //unauthorized
        List<Challenge> challenges = response.challenges();
        for (Challenge challenge : challenges) {
            String scheme = challenge.scheme();
            if (BEARER.equalsIgnoreCase(scheme)) {
                String refreshToken = getRefreshToken();
                retrofit2.Response<AccessToken> accessToken;

                if (refreshToken != null && !refreshToken.isEmpty()) {
                    accessToken = redditAuthClient.refreshToken().execute();
                } else {
                    // app only grant does not receive a refresh token
                    accessToken = redditAuthClient.getAccessToken().execute();
                }

                if (accessToken != null && accessToken.isSuccess() && accessToken.body().getAccessToken() != null) {
                    updateToken(accessToken.body());

                    return response.request().newBuilder()
                            .header("Authorization", BEARER + " " + getToken())
                            .build();
                } else {
                    listener.onAuthenticationRequest();
                }

            } else if (BASIC.equalsIgnoreCase(scheme)) {
                Log.d("Unauthorized", response.code() + " - Basic Auth failed.");

                if (response.request().header(REFRESH_HEADER_TAG).equalsIgnoreCase(REFRESH_HEADER_TAG)) {
                    // if refresh token mechanism is unauthorized return a message
                    Log.e("Unauth Refresh Token", response.code() + " - Refresh Token Failed");
                    listener.onAuthenticationRequest();
                }
            }
        }


        return null;
    }

    private void updateToken(AccessToken body) {
        databaseManager.setTokenInfo(body);
        //update to new token info
        databaseManager.setCurrentToken(databaseManager.getSessionUser().getTokenInfo());
    }

    private String getRefreshToken() {
        if (databaseManager.getCurrentToken() != null) {
            return databaseManager.getCurrentToken().getRefreshToken();
        }

        return null;
    }

    private String getToken() {
        if (databaseManager.getCurrentToken() != null) {
            return databaseManager.getCurrentToken().getToken();
        }

        return null;
    }
}
