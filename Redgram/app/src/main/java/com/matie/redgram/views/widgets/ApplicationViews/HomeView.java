package com.matie.redgram.views.widgets.ApplicationViews;

import com.matie.redgram.views.widgets.ApplicationViews.base.BaseContextView;
import com.matie.redgram.views.widgets.PostList.PostRecyclerView;

/**
 * Created by matie on 12/04/15.
 */
public interface HomeView extends BaseContextView {
    public void showProgress();
    public void hideProgress();
    public void showInfoMessage();
    public void showErrorMessage();
    public void showToolbar();
    public void hideToolbar();
    public PostRecyclerView getRecyclerView();
}
