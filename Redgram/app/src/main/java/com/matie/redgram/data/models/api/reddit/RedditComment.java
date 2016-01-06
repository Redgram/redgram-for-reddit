package com.matie.redgram.data.models.api.reddit;

import com.matie.redgram.data.models.api.reddit.base.RedditObject;

import java.util.List;

/**
 * Created by matie on 2016-01-04.
 */
public class RedditComment extends RedditSubmission {
    private String approved_by;
    private String author;
    private String author_flair_css_class;
    private String author_flair_text;
    private String banned_by;
    private String body;
    private String body_html;
    private boolean edited;
    private int gilded;
    private boolean likes;
    private String link_author;
    private String link_title;
    private String link_url;
    private int num_reports;
    private String parent_id;
    private boolean saved;
    private int score;
    private boolean score_hidden;
    private String subreddit;
    private String subreddit_id;
    private String distinguished;
    private List<RedditObject> replies;

    public String getApproved_by() {
        return approved_by;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    public String getAuthor_flair_css_class() {
        return author_flair_css_class;
    }

    public String getAuthor_flair_text() {
        return author_flair_text;
    }

    public String getBanned_by() {
        return banned_by;
    }

    public String getBody() {
        return body;
    }

    public String getBody_html() {
        return body_html;
    }

    public boolean isEdited() {
        return edited;
    }

    @Override
    public int getGilded() {
        return gilded;
    }

    public boolean isLikes() {
        return likes;
    }

    public String getLink_author() {
        return link_author;
    }

    public String getLink_title() {
        return link_title;
    }

    public String getLink_url() {
        return link_url;
    }

    public int getNum_reports() {
        return num_reports;
    }

    public String getParent_id() {
        return parent_id;
    }

    @Override
    public boolean isSaved() {
        return saved;
    }

    @Override
    public int getScore() {
        return score;
    }

    public boolean isScore_hidden() {
        return score_hidden;
    }

    @Override
    public String getSubreddit() {
        return subreddit;
    }

    public String getSubreddit_id() {
        return subreddit_id;
    }

    public String getDistinguished() {
        return distinguished;
    }

    public List<RedditObject> getReplies() {
        return replies;
    }
}
