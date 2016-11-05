package com.matie.redgram.data.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by matie on 2016-05-08.
 */
public class Subreddit extends RealmObject {
    @PrimaryKey
    private String name;
    private boolean subscribed;
    private String description;
    private long subscribersCount;
    private int accountsActive;
    private String subredditType;
    private String submissionType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public long getSubscribersCount() {
        return subscribersCount;
    }

    public void setAccountsActive(int accountsActive) {
        this.accountsActive = accountsActive;
    }

    public int getAccountsActive() {
        return accountsActive;
    }

    public void setSubredditType(String subredditType) {
        this.subredditType = subredditType;
    }

    public String getSubredditType() {
        return subredditType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }

    public String getSubmissionType() {
        return submissionType;
    }
}
