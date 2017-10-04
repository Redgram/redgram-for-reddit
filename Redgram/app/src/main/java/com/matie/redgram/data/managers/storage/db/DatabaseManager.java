package com.matie.redgram.data.managers.storage.db;

import android.content.Context;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.State;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.ui.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
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
@RealmModule(classes = {Session.class, User.class, Token.class, Prefs.class, State.class, Subreddit.class})
public class DatabaseManager {

    public static final Integer SESSION_DEFAULT_ID = 69;
    private RealmConfiguration configuration;
    private Token currentAccessToken;

    @Inject
    public DatabaseManager(App app) {
        this.configuration = new RealmConfiguration.Builder(app.getApplicationContext())
                .deleteRealmIfMigrationNeeded()
                .setModules(this)
                .build();
        Realm.setDefaultConfiguration(configuration);
        initCurrentAccessToken();
    }

    private void initCurrentAccessToken() {
        Realm realm = getInstance();
        Token token = DatabaseHelper.getSessionToken(realm);
        if(token != null){
            this.currentAccessToken = realm.copyFromRealm(token);
        }
    }


    public Token getCurrentToken() {
        return currentAccessToken;
    }

    public void setCurrentToken(Token currentToken) {
        this.currentAccessToken = currentToken;
    }

    public Realm getInstance(){
        return Realm.getDefaultInstance();
    }

    public RealmConfiguration getSessionConfig(){
        return configuration;
    }

    public void close(Realm realm){
        DatabaseHelper.close(realm);
    }

    @Deprecated
    public void deleteSession(Realm realm) {
        DatabaseHelper.deleteSession(realm);
        setCurrentToken(null);
    }

    public void setSession(AuthWrapper wrapper){
        Realm realm = getInstance();
        DatabaseHelper.setSession(realm, wrapper);
        close(realm);
    }

    public void setTokenInfo(AccessToken accessToken) {
        Realm realm = getInstance();
        DatabaseHelper.setTokenInfo(realm, accessToken);
        close(realm);
    }

    public void setPrefs(AuthPrefs prefs) {
        Realm realm = getInstance();
        DatabaseHelper.setPrefs(realm, prefs);
        close(realm);
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

    public List<User> getUsers(){
        Realm realm = getInstance();
        RealmResults<User> users = DatabaseHelper.getUsers(realm);
        List<User> usableUsers = new ArrayList<>();
        if(users != null && !users.isEmpty()){
            for(User user : users){
                usableUsers.add(realm.copyFromRealm(user));
            }
        }
        close(realm);
        return usableUsers;
    }

    public void setSubreddits(List<SubredditItem> items){
        Realm realm = getInstance();
        DatabaseHelper.setSubreddits(realm, items);
        close(realm);
    }

    public void setSubreddits(List<SubredditItem> items, String userId){
        Realm realm = getInstance();
        DatabaseHelper.setSubreddits(realm, items, userId);
        close(realm);
    }

    public List<Subreddit> getSubreddits() {
        Realm realm = getInstance();
        RealmList<Subreddit> subreddits = DatabaseHelper.getSubreddits(realm);
        List<Subreddit> usableList = realm.copyFromRealm(subreddits);
        close(realm);
        return usableList;
    }

    public List<Subreddit> getSubreddits(String userId) {
        Realm realm = getInstance();
        RealmList<Subreddit> subreddits = DatabaseHelper.getSubreddits(realm, userId);
        List<Subreddit> usableList = realm.copyFromRealm(subreddits);
        close(realm);
        return usableList;
    }
}
