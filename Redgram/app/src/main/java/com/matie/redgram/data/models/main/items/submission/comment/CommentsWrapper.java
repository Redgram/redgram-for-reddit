package com.matie.redgram.data.models.main.items.submission.comment;

import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;

import java.util.List;


public class CommentsWrapper {
    private List<SubmissionItem> commentItems;
    private PostItem postItem;

    public List<SubmissionItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(List<SubmissionItem> commentItem) {
        this.commentItems = commentItem;
    }

    public PostItem getPostItem() {
        return postItem;
    }

    public void setPostItem(PostItem postItem) {
        this.postItem = postItem;
    }
}
