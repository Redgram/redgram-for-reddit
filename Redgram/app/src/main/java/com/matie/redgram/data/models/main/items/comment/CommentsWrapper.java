package com.matie.redgram.data.models.main.items.comment;

import com.matie.redgram.data.models.main.items.PostItem;

import java.util.List;

/**
 * Created by matie on 2016-02-08.
 */
public class CommentsWrapper {
    private List<? extends CommentBaseItem> commentItems;
    private PostItem postItem;

    public List<? extends CommentBaseItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<? extends CommentBaseItem> commentItem) {
        this.commentItems = commentItem;
    }

    public PostItem getPostItem() {
        return postItem;
    }

    public void setPostItem(PostItem postItem) {
        this.postItem = postItem;
    }
}
