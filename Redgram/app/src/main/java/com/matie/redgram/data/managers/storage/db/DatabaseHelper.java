package com.matie.redgram.data.managers.storage.db;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.items.SubredditItem;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by matie on 2016-02-26.
 */
public class DatabaseHelper {

    public DatabaseHelper() {}

    public static void close(Realm realm){
        if(realm != null && !realm.isClosed()){
            realm.close();
        }
    }

    static Session getSession(Realm realm){
        return realm.where(Session.class).findFirst();
    }

    private static void setSession(Realm realm, Session session) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(session);
        realm.commitTransaction();
    }

    public static void setSession(Realm realm, AuthWrapper wrapper) {
        Token token = buildToken(wrapper.getAccessToken());
        Prefs prefs = buildPrefs(wrapper.getAuthPrefs());
        User user = buildUser(wrapper.getAuthUser(), token, prefs);
        setSession(realm, buildSession(user));
    }

    //updates everything except for refresh token - remains the same
    public static void setTokenInfo(Realm realm, AccessToken accessToken) {
        Session session = getSession(realm);
        if(session != null){
            realm.beginTransaction();

            Token token = session.getUser().getTokenInfo();
            token.setExpiresIn(accessToken.getExpiresIn());
            token.setTokenType(accessToken.getTokenType());
            token.setToken(accessToken.getAccessToken());
            token.setScope(accessToken.getScope());

            realm.copyToRealmOrUpdate(session);
            realm.commitTransaction();
        }
    }

    public static User getSessionUser(Realm instance) {
        Session session = getSession(instance);
        if(session != null){
            return session.getUser();
        }
        return null;
    }

    public static User getUserById(Realm realm, String userId) {
        return realm.where(User.class).equalTo("id", userId).findFirst();
    }

    public static Token getSessionToken(Realm instance) {
        User user = getSessionUser(instance);
        if(user != null){
            return user.getTokenInfo();
        }
        return null;
    }

    public static void deleteSession(Realm realm) {
        Session session = getSession(realm);
        if(session != null){
            realm.beginTransaction();
            session.removeFromRealm();
            realm.commitTransaction();
        }
    }

    public static RealmList<Subreddit> getSubreddits(Realm realm) {
        User user = getSessionUser(realm);
        if(user != null){
            return user.getSubreddits();
        }
        return null;
    }

    public static RealmList<Subreddit> getSubreddits(Realm realm, String userId) {
        User user = getUserById(realm, userId);
        if(user != null){
            return user.getSubreddits();
        }
        return null;
    }

    public static void setSubreddits(Realm realm, List<SubredditItem> items) {
        User user = getSessionUser(realm);
        if(user != null){
            setSubreddits(realm, user, items);
        }
    }

    public static void setSubreddits(Realm realm, List<SubredditItem> items, String userId) {
        User user = getUserById(realm, userId);
        if(user != null){
            setSubreddits(realm, user, items);
        }
    }

    public static void setSubreddits(Realm realm, User user, List<SubredditItem> items) {
        realm.beginTransaction();

        if(user.getSubreddits() != null){
            user.getSubreddits().clear();
        }else{
            user.setSubreddits(new RealmList<>());
        }
        for(SubredditItem item : items){
            user.getSubreddits().add(buildSubreddit(item));
        }

        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();
    }

    public static Session buildSession(User user){
        Session session = new Session();
        session.setId(DatabaseManager.id());
        session.setUser(user);
        return session;
    }

    public static Token buildToken(AccessToken accessToken) {
        Token token = new Token();
        token.setExpiresIn(accessToken.getExpiresIn());
        token.setRefreshToken(accessToken.getRefreshToken());
        token.setScope(accessToken.getScope());
        token.setToken(accessToken.getAccessToken());
        token.setTokenType(accessToken.getTokenType());
        return token;
    }

    public static User buildUser(AuthUser authUser, Token token, Prefs prefs) {
        User user = new User();
        user.setId(authUser.getId());
        user.setUserName(authUser.getName());
        user.setInboxCount(authUser.getInboxCount());
        user.setTokenInfo(token);
        user.setPrefs(prefs);
        return user;
    }

    public static Prefs buildPrefs(AuthPrefs authPrefs) {
        Prefs prefs = new Prefs();
        prefs.setDefaultCommentSort(authPrefs.getDefault_comment_sort());
        prefs.setHideDowns(authPrefs.isHide_downs());
        prefs.setLabelNsfw(authPrefs.isLabel_nsfw());
        prefs.setMinCommentsScore(authPrefs.getMin_comment_score());
        prefs.setMinLinkScore(authPrefs.getMin_link_score());
        prefs.setNumComments(authPrefs.getNum_comments());
        prefs.setOver18(authPrefs.isOver_18());
        prefs.setShowFlair(authPrefs.isShow_flair());
        prefs.setShowLinkFlair(authPrefs.isShow_link_flair());
        prefs.setShowTrending(authPrefs.isShow_trending());
        prefs.setStoreVisits(authPrefs.isStore_visits());
        return prefs;
    }

    public static Subreddit buildSubreddit(SubredditItem item) {
        Subreddit subreddit = new Subreddit();
        subreddit.setName(item.getName());
        subreddit.setDescription(item.getDescriptionHtml());
        subreddit.setSubscribersCount(item.getSubscribersCount());
        subreddit.setAccountsActive(item.getAccountsActive());
        subreddit.setSubredditType(item.getSubredditType());
        subreddit.setSubmissionType(item.getSubmissionType());
        return subreddit;
    }
}
