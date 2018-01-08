package com.matie.redgram.data.network.api.reddit.interceptors;


import android.util.Base64;
import android.util.Log;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.network.connection.ConnectionManager;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.matie.redgram.data.network.api.reddit.base.RedditServiceBase.CREDENTIALS;
import static com.matie.redgram.data.network.api.reddit.base.RedditServiceBase.OAUTH_HOST;
import static com.matie.redgram.data.network.api.reddit.base.RedditServiceBase.REDDIT_HOST;

public class RedditGeneralInterceptor implements Interceptor {

    public interface InterceptorListener {
        void onInterceptAuthRequest();
    }

    private static final int MAX_AGE = 60; //1 minute
    private static final int MAX_STALE = 60 * 60 * 24 * 28; // tolerate 4-weeks

    private final ConnectionManager connectionManager;
    private final DatabaseManager databaseManager;
    private final Set<InterceptorListener> listeners = new HashSet<>();

    @Inject
    public RedditGeneralInterceptor(ConnectionManager connectionManager,
                                    DatabaseManager databaseManager) {
        this.connectionManager = connectionManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final boolean isOnline = connectionManager.isOnline();

        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder();
        String authHeader = originalRequest.header("Authorization");
        String host = originalRequest.url().host();

        if (REDDIT_HOST.equalsIgnoreCase(host)) {
            requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
            requestBuilder.addHeader("accept", "application/json");
            requestBuilder.addHeader("Authorization", getCredentials());
        } else if (OAUTH_HOST.equalsIgnoreCase(host)) {
            requestBuilder.addHeader("content-type", "application/x-www-form-urlencoded");
            requestBuilder.addHeader("accept", "application/json");

            if (authHeader == null) {
                final String token = getToken();

                if (token == null) {
                    notifyListeners();
                    return null;
                }

                requestBuilder.addHeader("Authorization", "bearer "+ token);
            }

            if (!isOnline) {
                connectionManager.showConnectionStatus(false);
                requestBuilder.addHeader("Cache-Control",
                        "public, only-if-cached, max-stale=" + MAX_STALE);
                Log.d("CSTATUS", "no connection!, stale = " + MAX_STALE);
            }
        }

        Request request = requestBuilder.build();

        Response.Builder responseBuilder = chain.proceed(request).newBuilder();

        if (OAUTH_HOST.equalsIgnoreCase(originalRequest.url().host()) && isOnline) {
            responseBuilder.header("cache-control", "public, max-age=" + MAX_AGE);
            responseBuilder.header("Connection-Status", "Connected");
            Log.d("CSTATUS", "connected to internet! age = " + MAX_AGE + " seconds");
        }

        return responseBuilder.build();
    }

    private String getCredentials() {
        return "Basic " +
                Base64.encodeToString(CREDENTIALS.getBytes(), Base64.NO_WRAP);
    }

    public void addListener(InterceptorListener listener) {
        if (listener == null || listeners.contains(listener)) return;

        listeners.add(listener);
    }

    public void removeListener(InterceptorListener listener) {
        if (listener == null) return;

        listeners.remove(listener);
    }

    private void notifyListeners() {
        listeners.forEach(InterceptorListener::onInterceptAuthRequest);
    }

    private String getToken() {
        if (databaseManager.getCurrentToken() != null) {
            return databaseManager.getCurrentToken().getToken();
        }

        return null;
    }
}
