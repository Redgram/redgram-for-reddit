package com.matie.redgram.data.network.api.reddit.base;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.api.reddit.base.BooleanDate;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.utils.reddit.BooleanDateDeserializer;
import com.matie.redgram.data.utils.reddit.DateTimeDeserializer;
import com.matie.redgram.data.utils.reddit.RedditObjectDeserializer;

import org.joda.time.DateTime;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class RedditService extends RedditServiceBase {

    private static final int CACHE_DIR_SIZE = 10 * 1024 * 1024;

    protected final Context context;
    private final Retrofit.Builder retrofitBuilder;
    protected final DatabaseManager databaseManager;

    public RedditService(Context context,
                         DatabaseManager databaseManager) {
        this.context = context;
        this.databaseManager = databaseManager;
        this.retrofitBuilder = getRetrofitBuilder(context);
    }

    protected Retrofit buildRetrofit(String host) {
        return retrofitBuilder.baseUrl(host).build();
    }

    private Retrofit.Builder getRetrofitBuilder(final Context context) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(myHttpClient(context));
    }

    private OkHttpClient myHttpClient(final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        List<Interceptor> interceptors = getInterceptors();
        for (Interceptor interceptor : interceptors) {
            if (interceptor == null) continue;
            builder.addInterceptor(interceptor);
        }

        List<Authenticator> authenticators = getAuthenticators();
        for (Authenticator authenticator : authenticators) {
            if (authenticator == null) continue;
            builder.authenticator(authenticator);
        }

//        builder.addInterceptor(getCachingInterceptor());
        builder.cache(myCache(context));

        return builder.build();
    }

    protected abstract List<Authenticator> getAuthenticators();

    protected abstract List<Interceptor> getInterceptors();

    protected static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BooleanDate.class, new BooleanDateDeserializer())
                .registerTypeAdapter(RedditObject.class, new RedditObjectDeserializer())
                .registerTypeAdapter(DateTime.class, new DateTimeDeserializer())
                .create();
    }

    private Cache myCache(final Context context) {
        //setup cache
        File httpCacheDir = new File(context.getCacheDir(), "HttpCacheDir");
        return new Cache(httpCacheDir, CACHE_DIR_SIZE);
    }

    private Interceptor getCachingInterceptor() {
        return chain -> {
            Request request = chain.request();

            request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder()
                            .maxAge(1, TimeUnit.DAYS)
                            .minFresh(4, TimeUnit.HOURS)
                            .maxStale(8, TimeUnit.HOURS)
                            .build())
                    .url(request.url())
                    .build();


            return chain.proceed(request);
        };
    }

    protected String getRefreshToken() {
        if (databaseManager.getCurrentToken() != null) {
            return databaseManager.getCurrentToken().getRefreshToken();
        }

        return null;
    }

    protected String getToken() {
        if (databaseManager.getCurrentToken() != null) {
            return databaseManager.getCurrentToken().getToken();
        }

        return null;
    }
}
