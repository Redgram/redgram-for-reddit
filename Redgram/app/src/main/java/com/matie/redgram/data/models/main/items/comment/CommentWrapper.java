package com.matie.redgram.data.models.main.items.comment;

import com.matie.redgram.data.models.main.items.PostItem;

import java.util.List;

/**
 * Created by matie on 2016-02-08.
 */
public class CommentWrapper {
    private List<? extends CommentBaseItem> commentItem;
    private PostItem postItem;

    public List<? extends CommentBaseItem> getCommentItems() {
        return commentItem;
    }

    public void setCommentItems(List<? extends CommentBaseItem> commentItem) {
        this.commentItem = commentItem;
    }

    public PostItem getPostItem() {
        return postItem;
    }

    public void setPostItem(PostItem postItem) {
        this.postItem = postItem;
    }
}
