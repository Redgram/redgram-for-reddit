package com.matie.redgram.data.managers.storage.db;

import android.content.Context;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Settings;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.ui.App;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.annotations.RealmModule;

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
@RealmModule(classes = {Session.class, User.class, Token.class, Settings.class, Prefs.class, Subreddit.class})
public class DatabaseManager {

//    public static final String DB_NAME = "redgram.realm.session";
    public static final Integer SESSION_DEFAULT_ID = 69;
    private RealmConfiguration configuration;
    private final Context context;

    private String currentAccessToken;

    @Inject
    public DatabaseManager(App app) {
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
        DatabaseHelper.close(realm);
    }

    public void deleteSession(Realm realm) {
        DatabaseHelper.deleteSession(realm);
        setCurrentToken(null);
    }

    public void setSession(AuthWrapper wrapper){
        Realm realm = getInstance();
        DatabaseHelper.setSession(realm, wrapper);
        close(realm);

        setCurrentToken(wrapper.getAccessToken().getAccessToken());
    }

    public void setTokenInfo(AccessToken accessToken) {
        Realm realm = getInstance();
        DatabaseHelper.setTokenInfo(realm, accessToken);
        close(realm);

        setCurrentToken(accessToken.getAccessToken());
    }

    public Session getSession(){
        Realm realm = getInstance();
        Session session = DatabaseHelper.getSession(realm);
        Session usableSession = realm.copyFromRealm(session);
        close(realm);
        return usableSession;
    }

    public User getSessionUser(){
        Realm realm = getInstance();
        User user = DatabaseHelper.getSessionUser(realm);
        User usableUser = null;
        if(user != null){
            usableUser = realm.copyFromRealm(user);
        }
        close(realm);
        return usableUser;
    }

    public String getToken(){
        Realm realm = getInstance();
        Token token = DatabaseHelper.getSessionToken(realm);

        if(token != null){
            return token.getToken();
        }

        close(realm);
        return null;
    }

    public String getRefreshToken(){
        Realm realm = getInstance();
        Token token = DatabaseHelper.getSessionToken(realm);

        if(token != null){
            return token.getRefreshToken();
        }

        close(realm);
        return null;
    }

    public void setSubreddits(List<SubredditItem> items){
        Realm realm = getInstance();
        User user = DatabaseHelper.getSessionUser(realm);
        if(user != null){
            realm.beginTransaction();
            if(user.getSubreddits() != null){
                user.getSubreddits().clear();
            }else{
                user.setSubreddits(new RealmList<>());
            }
            for(SubredditItem item : items){
                Subreddit subreddit = new Subreddit();
                subreddit.setName(item.getName());
                user.getSubreddits().add(subreddit);
            }
            realm.copyToRealmOrUpdate(user);
            realm.commitTransaction();
        }
        close(realm);
    }

    public void setSubreddits(List<SubredditItem> items, String userId){
        Realm realm = getInstance();
        User user = realm.where(User.class).equalTo("id", userId).findFirst();
        if(user != null){
            //duplicate
            realm.beginTransaction();
            if(user.getSubreddits() != null){
                user.getSubreddits().clear();
            }else{
                user.setSubreddits(new RealmList<>());
            }
            for(SubredditItem item : items){
                Subreddit subreddit = new Subreddit();
                subreddit.setName(item.getName());
                user.getSubreddits().add(subreddit);
            }
            realm.copyToRealmOrUpdate(user);
            realm.commitTransaction();
        }
        close(realm);
    }

    public RedditListing<SubredditItem> getSubreddits() {
        Realm realm = getInstance();
        RedditListing<SubredditItem> subreddits = DatabaseHelper.getSubreddits(realm);
        close(realm);
        return subreddits;
    }

    public RedditListing<SubredditItem> getSubreddits(String userId) {
        Realm realm = getInstance();
        RedditListing<SubredditItem> subreddits = DatabaseHelper.getSubreddits(realm, userId);
        close(realm);
        return subreddits;
    }
}
