package com.matie.redgram.data.models.api.reddit.main;

import com.matie.redgram.data.models.api.reddit.base.RedditObject;

import org.joda.time.DateTime;

/**
 * Created by matie on 2016-08-07.
 */
public class RedditUser extends RedditObject {
    private String id;
    private String name;
    private DateTime created_utc;
    private int link_karma;
    private int comment_karma;
    private boolean has_verified_email;
    private boolean has_subscribed;
    private boolean is_friend;
    private boolean is_gold;
    private boolean is_mod;

    public DateTime getDate() {
        return created_utc;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLinkKarma() {
        return link_karma;
    }

    public int getCommentKarma() {
        return comment_karma;
    }

    public boolean isHasVerifiedEmail() {
        return has_verified_email;
    }

    public boolean isHasSubscribed() {
        return has_subscribed;
    }

    public boolean isFriend() {
        return is_friend;
    }

    public boolean isGold() {
        return is_gold;
    }

    public boolean isMod() {
        return is_mod;
    }
}
