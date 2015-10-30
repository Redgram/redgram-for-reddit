package com.matie.redgram.data.models.api.reddit;

import com.matie.redgram.data.models.api.reddit.base.RedditObject;

/**
 * Created by matie on 2015-10-21.
 */
public class RedditSubreddit extends RedditObject {
    int accounts_active;
    int comment_score_hide_mins;
    String description;
    String description_html;
    String display_name;
    String header_img;
    String header_title;
    boolean over18;
    boolean public_traffic;
    long subscribers;
    String submission_type;
    String title;
    String url;
    boolean user_is_banned;
    boolean user_is_contributor;
    boolean user_is_moderator;
    boolean user_is_subscriber;

    public int getAccountsActive() {
        return accounts_active;
    }

    public int getCommentScoreHideMins() {
        return comment_score_hide_mins;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionHtml() {
        return description_html;
    }

    public String getDisplayName() {
        return display_name;
    }

    public String getHeaderImg() {
        return header_img;
    }

    public String getHeaderTitle() {
        return header_title;
    }

    public boolean isOver18() {
        return over18;
    }

    public boolean isPublicTraffic() {
        return public_traffic;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public String getSubmission_type() {
        return submission_type;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isUserBanned() {
        return user_is_banned;
    }

    public boolean isUserContributor() {
        return user_is_contributor;
    }

    public boolean isUserModerator() {
        return user_is_moderator;
    }

    public boolean isUserSubscriber() {
        return user_is_subscriber;
    }
}
