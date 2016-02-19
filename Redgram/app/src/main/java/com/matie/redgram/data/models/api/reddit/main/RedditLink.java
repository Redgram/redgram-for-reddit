package com.matie.redgram.data.models.api.reddit.main;

/**
 * Created by matie on 17/04/15.
 */
public class RedditLink extends RedditSubmission {
    private String domain;
    private String selftext_html;
    private String selftext;
    private String link_flair_text;
    private boolean clicked;
    private boolean hidden;
    private String thumbnail;
    private boolean is_self;
    private String permalink;
    private boolean stickied;
    private String url;
    private String title;
    private int num_comments;
    private boolean visited;
    private boolean over_18;
    private String distinguished;

    public String isDistinguished() {
        return distinguished;
    }

    public boolean isAdult() {
        return over_18;
    }

    public String getDomain() {
        return domain;
    }

    public String getSelftextHtml() {
        return selftext_html;
    }

    public String getSelftext() {
        return selftext;
    }

    public String getLinkFlairText() {
        return link_flair_text;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isHidden() {
        return hidden;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public boolean isSelf() {
        return is_self;
    }

    public String getPermalink() {
        return permalink;
    }

    public boolean isStickied() {
        return stickied;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public int getNumComments() {
        return num_comments;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
