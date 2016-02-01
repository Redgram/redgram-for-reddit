package com.matie.redgram.data.models.main.items;

import com.matie.redgram.data.models.main.reddit.RedditObject;

import java.util.List;

/**
 * Created by matie on 2016-01-31.
 */
public class CommentItem extends RedditObject {
    private String author;
    private String body;
    private String bodyHtml;
    private String parent_id;
    private String subreddit_id;
    private List<CommentItem> replies;

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

    public String getParentId() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSubredditId() {
        return subreddit_id;
    }

    public void setSubredditId(String subreddit_id) {
        this.subreddit_id = subreddit_id;
    }

    public List<CommentItem> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentItem> replies) {
        this.replies = replies;
    }
}
