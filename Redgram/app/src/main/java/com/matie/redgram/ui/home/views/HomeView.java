package com.matie.redgram.ui.home.views;

import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

/**
 * Created by matie on 12/04/15.
 * // TODO: 2015-10-31 make it interact with the siding panel
 */
public interface HomeView extends BaseContextView {
    public void showLoading();
    public void hideLoading();
    public void showLoadMoreIndicator();
    public void hideLoadMoreIndicator();
    public void showInfoMessage();
    public void showErrorMessage(String error);
    public void showToolbar();
    public void hideToolbar();

    public PostRecyclerView getRecyclerView();
}
