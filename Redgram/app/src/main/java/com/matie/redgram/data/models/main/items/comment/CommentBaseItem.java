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
    private int level;
    private boolean isExpanded = true; //expanded by default
    private boolean isGrouped = false;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
    }

    public boolean isGrouped() {
        return isGrouped;
    }

    public void setIsGrouped(boolean isGrouped) {
        this.isGrouped = isGrouped;
    }

    @Override
    public boolean equals(Object o) {
        boolean flag = false;

        if(o instanceof CommentBaseItem){
            CommentBaseItem item = (CommentBaseItem) o;
            flag = this.id.equalsIgnoreCase(item.getId());
        }

        return flag;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
