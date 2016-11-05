package com.matie.redgram.data.models.api.reddit.auth;

import org.joda.time.DateTime;


/**
 * Created by matie on 2016-02-26.
 */
public class AuthUser {
    private boolean has_mail;
    private String name;
    private DateTime created;
    private boolean hide_from_robots;
    private boolean is_suspended;
    private DateTime created_utc;
    private boolean has_mod_mail;
    private int link_karma;
    private int comment_karma;
    private boolean over_18;
    private boolean is_gold;
    private boolean is_mod;
    private String id;
    private DateTime gold_expiration;
    private int inbox_count;
    private boolean has_verified_email;
    private int gold_creddits;
    private DateTime suspension_expiration_utc;

    public boolean isHasMail() {
        return has_mail;
    }

    public String getName() {
        return name;
    }

    public DateTime getCreated() {
        return created;
    }

    public boolean isHideFromRobots() {
        return hide_from_robots;
    }

    public boolean isSuspended() {
        return is_suspended;
    }

    public DateTime getCreateUtc() {
        return created_utc;
    }

    public boolean isHasModMail() {
        return has_mod_mail;
    }

    public int getLinkKarma() {
        return link_karma;
    }

    public int getCommentKarma() {
        return comment_karma;
    }

    public boolean isOver18() {
        return over_18;
    }

    public boolean isGold() {
        return is_gold;
    }

    public boolean isMod() {
        return is_mod;
    }

    public String getId() {
        return id;
    }

    public DateTime getGoldExpiration() {
        return gold_expiration;
    }

    public int getInboxCount() {
        return inbox_count;
    }

    public boolean isHasVerifiedEmail() {
        return has_verified_email;
    }

    public int getGoldCreddits() {
        return gold_creddits;
    }

    public DateTime getSuspensionExpirationUtc() {
        return suspension_expiration_utc;
    }

    public void setToDefault(){
        id = "Guest";
        name = "Guest";
        hide_from_robots = false;
        is_suspended = false;
        created_utc = new DateTime();
        has_mod_mail = false;
        link_karma = 0;
        comment_karma = 0;
        over_18 = false;
        is_gold = false;
        is_mod = false;
        inbox_count = 0;
        has_verified_email = false;
    }
}
