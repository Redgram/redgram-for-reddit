package com.matie.redgram.data.managers.storage.db.session;

import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Token;
import com.matie.redgram.data.models.db.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by matie on 2016-02-26.
 */
public class SessionHelper {

    public SessionHelper() {}

    public static void close(Realm realm){
        if(realm != null && !realm.isClosed()){
            realm.close();
        }
    }

    public static Session getSession(Realm realm){
        return realm.where(Session.class).findFirst();
    }

    public static void setSession(Realm realm, Session session) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(session);
        realm.commitTransaction();
    }

    public static void setSession(Realm realm, AuthWrapper wrapper) {
        Session session = new Session();
        Token token = new Token();
        token.setExpiresIn(wrapper.getAccessToken().getExpiresIn());
        token.setRefreshToken(wrapper.getAccessToken().getRefreshToken());
        token.setScope(wrapper.getAccessToken().getScope());
        token.setToken(wrapper.getAccessToken().getAccessToken());
        token.setTokenType(wrapper.getAccessToken().getTokenType());

        User user = new User();
        user.setId(wrapper.getAuthUser().getId());
        user.setUserName(wrapper.getAuthUser().getName());
        user.setInboxCount(wrapper.getAuthUser().getInboxCount());
        user.setTokenInfo(token);

        session.setId(SessionManager.id());
        session.setUser(user);

        setSession(realm, session);
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
}
