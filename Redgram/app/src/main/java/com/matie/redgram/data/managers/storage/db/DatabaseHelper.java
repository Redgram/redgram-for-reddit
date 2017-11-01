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
import io.realm.RealmResults;
import rx.Observable;

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

    public static Session getSession(Realm realm){
        return realm.where(Session.class).findFirst();
    }

    private static void setSession(Realm realm, Session session) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(session);
        realm.commitTransaction();
    }

    public static void  setSession(Realm realm, AuthWrapper wrapper) {
        if(wrapper.getAccessToken() != null){
            Token token = buildToken(wrapper.getAccessToken());
            AuthPrefs authPrefs = null;
            AuthUser authUser = null;
            if(User.USER_AUTH.equalsIgnoreCase(wrapper.getType())){
                authPrefs = wrapper.getAuthPrefs();
                authUser = wrapper.getAuthUser();
                token.setHolderType(User.USER_AUTH);
            }else if(User.USER_GUEST.equalsIgnoreCase(wrapper.getType())){
                authPrefs = new AuthPrefs();
                authPrefs.setToDefault();
                authUser = new AuthUser();
                authUser.setToDefault();
                token.setHolderType(User.USER_GUEST);
            }
            if(authPrefs != null && authUser != null){
                Prefs prefs = buildPrefs(authPrefs);

                //set id to be equal to the user's
                token.setId(authUser.getId());
                prefs.setId(authUser.getId());

                User user = buildUser(authUser, token, prefs);
                user.setUserType(wrapper.getType());
                setSession(realm, buildSession(user));
            }
        }
    }

    //updates everything except for refresh token - remains the same
    public static void setTokenInfo(Realm realm, AccessToken accessToken) {
        User user = getSessionUser(realm);
        if(user != null){
            realm.beginTransaction();

            Token token = user.getTokenInfo();
            if(token != null){
                token.setExpiresIn(accessToken.getExpiresIn());
                token.setTokenType(accessToken.getTokenType());
                token.setToken(accessToken.getAccessToken());
                token.setScope(accessToken.getScope());
            }

            realm.copyToRealmOrUpdate(token);
            realm.commitTransaction();
        }
    }

    public static void setPrefs(Realm realm, AuthPrefs prefs) {
        User user = getSessionUser(realm);
        if(user != null){
            realm.beginTransaction();

            Prefs newPrefs = buildPrefs(prefs);
            newPrefs.setId(user.getId());

            realm.copyToRealmOrUpdate(newPrefs);
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

    public static RealmResults<User> getUsers(Realm realm) {
        return realm.where(User.class).findAll();
    }

    public static User getUserById(Realm realm, String userId) {
        return realm.where(User.class).equalTo("id", userId).findFirst();
    }

    public static Prefs getPrefs(Realm realm) {
        User sessionUser = getSessionUser(realm);
        if (sessionUser != null) {
            return getPrefsByUserId(realm, sessionUser.getId());
        }

        return null;
    }

    public static Prefs getPrefsByUserId(Realm realm, String userId) {
        return realm.where(Prefs.class).equalTo("id", userId).findFirst();
    }

    public static Observable<RealmResults<User>> getUsersAsync(Realm realm) {
        return realm.where(User.class).findAllAsync().asObservable();
    }

    public static Observable<User> getUserByIdAsync(Realm realm, String userId) {
        return realm.where(User.class).equalTo("id", userId).findFirstAsync().asObservable();
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

            if(session.getUser() != null){
                deleteUser(session.getUser());
            }
            session.removeFromRealm();
            realm.commitTransaction();
        }
    }

    public static void deleteUser(Realm realm, User user, Realm.Transaction.Callback callback){
        if(callback != null){
            realm.executeTransaction(realmRef -> deleteUser(user), callback);
        }else{
            realm.executeTransaction(realmRef -> deleteUser(user));
        }
    }

    public static void deleteUserById(Realm realm, String id, Realm.Transaction.Callback callback){
        User user = getUserById(realm, id);
        if(user != null){
            deleteUser(realm, user, callback);
        }
    }

    private static void deleteUser(User user){
        user.getPrefs().removeFromRealm();
        user.getTokenInfo().removeFromRealm();
        user.removeFromRealm();
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
        session.setId(DatabaseManager.SESSION_DEFAULT_ID);
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
        prefs.setDefaultCommentSort(authPrefs.getDefaultCommentSort());
        prefs.setHideDowns(authPrefs.isHideDowns());
        prefs.setHideUps(authPrefs.isHideUps());
        prefs.setLabelNsfw(authPrefs.isLabelNsfw());
        prefs.setMinCommentsScore(authPrefs.getMinCommentScore());
        prefs.setMinLinkScore(authPrefs.getMinLinkScore());
        prefs.setNumComments((authPrefs.getNumComments() == 0) ? 100 : authPrefs.getNumComments());
        prefs.setNumSites((authPrefs.getNumSites() == 0) ? 25 : authPrefs.getNumSites());
        prefs.setOver18(authPrefs.isOver18());
        prefs.setShowFlair(authPrefs.isShowFlair());
        prefs.setShowLinkFlair(authPrefs.isShowLinkFlair());
        prefs.setShowTrending(authPrefs.isShowTrending());
        prefs.setStoreVisits(authPrefs.isStoreVisits());
        prefs.setMedia(authPrefs.getMedia());
        prefs.setHighlightControversial(authPrefs.isHighlightControversial());
        prefs.setIgnoreSuggestedSort(authPrefs.getIgnoreSuggestedSort());
        //app-only
        prefs.setEnableRecentPost(true);
        prefs.setDisableNsfwPreview(true);
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
