package com.matie.redgram.ui.thread.views;

import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;

public interface ThreadView extends ContentView {
    String TRUE = "true";
    String FALSE = "false";
    int UP_VOTE = 1;
    int DOWN_VOTE = -1;
    int UN_VOTE = 0;

    void toggleVote(@Nullable int direction);
    void toggleSave(boolean save);
    void toggleUnHide();
    void passDataToCommentsView(List<SubmissionItem> commentItems);

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
