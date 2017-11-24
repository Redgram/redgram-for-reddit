package com.matie.redgram.ui.submission.links.views;


import android.content.Context;

import com.matie.redgram.ui.common.views.ContentView;

public interface SingleLinkView extends ContentView {
    void showHideUndoOption(final Context context);
    void votePost(int position, Integer dir);
    void savePost(int position, boolean save);
    void hidePost(int position);
    void reportPost(final Context context, int position);
    void deletePost(final Context context, int position);
    void loadCommentsForPost(final Context context, int position);

    void sharePost(final Context context, int position);
    void visitSubreddit(final Context context, String subredditName);
    void visitProfile(final Context context, String username);
    void openInBrowser(final Context context, int position);
    void copyItemLink(final Context context, int position);
    void viewWebMedia(final Context context, int position);
    void viewImageMedia(final Context context, int position, boolean loaded);
    void callAgeConfirmDialog(final Context context);
}
