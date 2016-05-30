package com.matie.redgram.ui.subcription.views;

import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditRecyclerView;

/**
 * Created by matie on 2015-11-24.
 */
public interface SubscriptionView extends BaseContextView {
    public void showLoading();
    public void hideLoading();
    public void showInfoMessage();
    public void showErrorMessage(String error);
    public SubredditRecyclerView getRecyclerView();
}
