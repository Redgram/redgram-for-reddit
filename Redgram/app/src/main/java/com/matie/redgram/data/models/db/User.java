package com.matie.redgram.data.models.db;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by matie on 2016-02-25.
 */
public class User extends RealmObject {
    @PrimaryKey
    private String id;
    private String userName;
    private int inboxCount;
    private Token tokenInfo;
    private Prefs prefs;
    private State state;
    private RealmList<Subreddit> subreddits;
    //add other stuff like settings and the like

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Token getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(Token tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public int getInboxCount() {
        return inboxCount;
    }

    public void setInboxCount(int inboxCount) {
        this.inboxCount = inboxCount;
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public void setPrefs(Prefs prefs) {
        this.prefs = prefs;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public RealmList<Subreddit> getSubreddits() {
        return subreddits;
    }

    public void setSubreddits(RealmList<Subreddit> subreddits) {
        this.subreddits = subreddits;
    }
}
