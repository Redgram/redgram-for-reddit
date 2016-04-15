package com.matie.redgram.ui.posts.views;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.views.ContentView;

import java.util.List;
import java.util.Map;

/**
 * Created by matie on 2016-03-16.
 */
public interface LinksView extends ContentView{
    int UP_VOTE = 1;
    int DOWN_VOTE = -1;
    int UN_VOTE = 0;

    PostItem getItem(int position);
    List<PostItem> getItems();
    void updateList();
    void updateList(List<PostItem> items);
    void refreshView();
    void refreshView(@Nullable String subredditChoice, @NonNull String filterChoice, @NonNull Map<String, String> params);
    void sortView(@NonNull String filterChoice, @Nullable Map<String, String> params);
    void search(String subredditChoice, @NonNull Map<String, String> params);

    void showHideUndoOption();
    void votePost(int position, Integer dir);
    void savePost(int position, boolean save);
    void hidePost(int position);
    void reportPost(int position);
    void deletePost(int position);
    void loadCommentsForPost(int position);

    void sharePost(int position);
    void visitSubreddit(String subredditName);
    void visitProfile(int position);
    void openInBrowser(int position);
    void copyItemLink(int position);
    void viewWebMedia(int position);
    void viewImageMedia(int position, boolean loaded);
}
