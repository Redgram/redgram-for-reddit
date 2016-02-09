package com.matie.redgram.data.models.main.items.comment;

import com.matie.redgram.data.models.main.reddit.RedditObject;

import java.util.List;

/**
 * Created by matie on 2016-01-31.
 */
public class CommentBaseItem extends RedditObject {

    public enum CommentType{
        REGULAR,
        MORE
    }

    private String id;
    private String parentId;
    private CommentType commentType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parent_id) {
        this.parentId = parent_id;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }
}
