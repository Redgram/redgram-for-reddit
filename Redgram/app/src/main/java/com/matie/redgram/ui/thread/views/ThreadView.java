package com.matie.redgram.ui.thread.views;

import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;

/**
 * Created by matie on 2016-02-10.
 */
public interface ThreadView extends ContentView {
    String TRUE = "true";
    String FALSE = "false";
    int UP_VOTE = 1;
    int DOWN_VOTE = -1;
    int UN_VOTE = 0;

    void toggleVote(@Nullable int direction);
    void passDataToCommentsView(List<CommentBaseItem> commentItems);

    //helper
    void sharePost();
    void visitSubreddit();
    void visitProfile();
    void openInBrowser();
    void copyItemLink();
    void viewMedia();

    //presenter
    void savePost();
    void hidePost();
    void reportPost();
    void deletePost();
}
