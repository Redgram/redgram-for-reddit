package com.matie.redgram.ui.comments.views;

import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditRecyclerView;

/**
 * Created by matie on 2016-01-04.
 */
public interface CommentsView extends BaseContextView {
    public void showLoading();
    public void hideLoading();

    //comments related
    public void expandItem(int position);
    public void collapseItem(int position);
    public void expandAll(int position);
    public void collapseAll(int position);
    public void hideItem(int position);
    public void scrollTo(int position);
    public void showLoadMoreIndicator();
    public void hideLoadMoreIndicator();

    public void showInfoMessage();
    public void showErrorMessage(String error);
}
