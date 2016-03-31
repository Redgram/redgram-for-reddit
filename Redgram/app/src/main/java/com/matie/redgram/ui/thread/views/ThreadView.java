package com.matie.redgram.ui.thread.views;

import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.thread.views.adapters.CommentsAdapter;
import com.matie.redgram.ui.thread.views.adapters.CommentsPagerAdapter;

import java.util.List;

/**
 * Created by matie on 2016-02-10.
 */
public interface ThreadView extends BaseContextView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
    void toggleVote(@Nullable int direction);
    void commentIndicator(boolean commentExists);
    void passDataToCommentsView(List<CommentBaseItem> commentItems);
}
