package com.matie.redgram.data.models.main.items.comment;

import com.matie.redgram.data.models.api.reddit.base.BooleanDate;
import com.matie.redgram.data.models.main.reddit.RedditListing;

import java.util.List;

/**
 * Created by matie on 2016-02-08.
 */
public class CommentItem extends CommentBaseItem {
    private String author;
    private String body;
    private String bodyHtml;
    private String link_id;
    private String subreddit_id;
    private BooleanDate edited;

    //UI state related
    private boolean hasReplies = false; //no replies by default
    private int childCount;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getLinkId() {
        return link_id;
    }

    public void setLinkId(String link_id) {
        this.link_id = link_id;
    }

    public String getSubredditId() {
        return subreddit_id;
    }

    public void setSubredditId(String subreddit_id) {
        this.subreddit_id = subreddit_id;
    }

    public boolean hasReplies() {
        return hasReplies;
    }

    public void setHasReplies(boolean hasReplies) {
        this.hasReplies = hasReplies;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public BooleanDate getEdited() {
        return edited;
    }

    public void setEdited(BooleanDate edited) {
        this.edited = edited;
    }
}
