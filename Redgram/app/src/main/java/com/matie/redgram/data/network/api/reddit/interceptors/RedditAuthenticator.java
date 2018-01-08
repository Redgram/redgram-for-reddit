package com.matie.redgram.data.network.api.reddit.interceptors;


import android.util.Log;
import android.widget.Toast;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.network.api.reddit.auth.RedditAuthInterface;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public interface AuthenticatorListener {
        void onAuthRequested();
    }

    private final DatabaseManager databaseManager;
    private final ToastHandler toastHandler;
    private final Set<AuthenticatorListener> listeners = new HashSet<>();

    @Inject
    public RedditAuthenticator(ToastHandler toastHandler,
                               DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.toastHandler = toastHandler;
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
                    notifyListeners();
                }

            } else if (BASIC.equalsIgnoreCase(scheme)) {
                Log.d("Unauthorized", response.code() + " - Basic Auth failed.");

                if (response.request().header(REFRESH_HEADER_TAG).equalsIgnoreCase(REFRESH_HEADER_TAG)) {
                    // if refresh token mechanism is unauthorized return a message
                    toastHandler.showBackgroundToast(response.code() + " - Unauthorized Refresh Token", Toast.LENGTH_LONG);
                    notifyListeners();
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

    public void addListener(AuthenticatorListener listener) {
        if (listener == null || listeners.contains(listener)) return;

        listeners.add(listener);
    }

    public void removeListener(AuthenticatorListener listener) {
        if (listener == null) return;

        listeners.remove(listener);
    }

    private void notifyListeners() {
        listeners.forEach(AuthenticatorListener::onAuthRequested);
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
