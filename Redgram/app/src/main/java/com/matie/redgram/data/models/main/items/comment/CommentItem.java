package com.matie.redgram.data.models.main.items.comment;

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
    private List<CommentBaseItem> replies; //might not be needed..establish a parent child relationship using ID's

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

    public List<CommentBaseItem> getReplies() {
        return replies;
    }

    public void setReplies(List<CommentBaseItem> replies) {
        this.replies = replies;
    }

}
