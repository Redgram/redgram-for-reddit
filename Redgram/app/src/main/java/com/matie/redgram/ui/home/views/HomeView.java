package com.matie.redgram.ui.home.views;

import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.home.views.widgets.postlist.PostRecyclerView;

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
