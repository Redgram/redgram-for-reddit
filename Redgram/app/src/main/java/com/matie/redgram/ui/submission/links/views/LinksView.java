package com.matie.redgram.ui.submission.links.views;

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
}
