package com.matie.redgram.data.managers.storage.db.session;

import android.content.Context;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.ui.App;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;
import retrofit2.Response;

/**
 * Session is only concerned with current user flow and credentials. The reason we are closing the
 * realm is because different threads are accessing the realm so making sure that it's closed is
 * recommended. Currently, passing the realm instance between threads is not supported.
 *
 * todo
 * In case the session was used on the UI thread, there is no need to obtain a new instance every time.
 * Therefore, obtain the default realm instance and pass it to methods of this class, and then close
 * it onDestroy() of the UI thread, instead after each method call.
 *
 * Created by matie on 2016-02-26.
 */
@RealmModule(classes = {Session.class, User.class, Token.class})
public class SessionManager  {

//    public static final String DB_NAME = "redgram.realm.session";
    public static final Integer SESSION_DEFAULT_ID = 69;
    private RealmConfiguration configuration;
    private final Context context;

    private String currentAccessToken;

    @Inject
    public SessionManager(App app) {
        this.context = app.getApplicationContext();
        this.configuration = new RealmConfiguration.Builder(context)
//                .name(DB_NAME)
                .setModules(this)
                .build();
        Realm.setDefaultConfiguration(configuration);
        this.currentAccessToken = getToken();
    }

    public static Integer id() {
        return SESSION_DEFAULT_ID;
    }

    public String getCurrentToken() {
        return currentAccessToken;
    }

    public void setCurrentToken(String currentAccessToken) {
        this.currentAccessToken = currentAccessToken;
    }

    public static Realm getInstance(){
        return Realm.getDefaultInstance();
    }


    public RealmConfiguration getSessionConfig(){
        return configuration;
    }

    public void close(Realm realm){
        SessionHelper.close(realm);
    }

    public void deleteSession(Realm realm) {
        SessionHelper.deleteSession(realm);
        setCurrentToken(null);
    }

    public void setSession(AuthWrapper wrapper){
        Realm realm = getInstance();
        SessionHelper.setSession(realm, wrapper);
        close(realm);

        setCurrentToken(wrapper.getAccessToken().getAccessToken());
    }

    public void setTokenInfo(AccessToken accessToken) {
        Realm realm = getInstance();
        SessionHelper.setTokenInfo(realm, accessToken);
        close(realm);

        setCurrentToken(accessToken.getAccessToken());
    }

    public Session getSession(){
        Realm realm = getInstance();
        Session session = SessionHelper.getSession(realm);
        Session usableSession = realm.copyFromRealm(session);
        close(realm);
        return usableSession;
    }

    public User getSessionUser(){
        Realm realm = getInstance();
        User user = SessionHelper.getSessionUser(realm);
        User usableUser = realm.copyFromRealm(user);
        close(realm);
        return usableUser;
    }

    public String getToken(){
        Realm realm = getInstance();
        Token token = SessionHelper.getSessionToken(realm);

        if(token != null){
            return token.getToken();
        }

        close(realm);
        return null;
    }

    public String getRefreshToken(){
        Realm realm = getInstance();
        Token token = SessionHelper.getSessionToken(realm);

        if(token != null){
            return token.getRefreshToken();
        }

        close(realm);
        return null;
    }

}
