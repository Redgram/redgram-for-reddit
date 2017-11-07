package com.matie.redgram.ui.submissions.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;
import java.util.Map;


public interface LinksView extends ContentView {
    int UP_VOTE = 1;
    int DOWN_VOTE = -1;
    int UN_VOTE = 0;

    PostItem getItem(int position);
    List<PostItem> getItems();
    void updateList();
    void updateRestOfList(int position);
    void updateItem(int position, PostItem postItem);
    PostItem removeItem(int position);
    void insertItem(int position, PostItem postItem);
    void updateList(List<PostItem> items);
    void refreshView();
    void refreshView(@Nullable String subredditChoice, @NonNull String filterChoice, @NonNull Map<String, String> params);
    void sortView(@NonNull String filterChoice, @Nullable Map<String, String> params);
    void search(String subredditChoice, @NonNull Map<String, String> params);

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
