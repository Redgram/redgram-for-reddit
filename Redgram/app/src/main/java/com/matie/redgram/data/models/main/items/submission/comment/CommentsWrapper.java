package com.matie.redgram.data.models.main.items.submission.comment;

import com.matie.redgram.data.models.main.items.submission.PostItem;

import java.util.List;


public class CommentsWrapper {
    private List<CommentBaseItem> commentItems;
    private PostItem postItem;

    public List<CommentBaseItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<CommentBaseItem> commentItem) {
        this.commentItems = commentItem;
    }

    public PostItem getPostItem() {
        return postItem;
    }

    public void setPostItem(PostItem postItem) {
        this.postItem = postItem;
    }
}
