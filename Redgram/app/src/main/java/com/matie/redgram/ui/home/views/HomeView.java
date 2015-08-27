package com.matie.redgram.ui.home.views;

import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.common.views.BaseContextView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;

/**
 * Created by matie on 12/04/15.
 */
public interface HomeView extends BaseContextView {
    public void showLoading();
    public void hideLoading();
    public void showLoadMoreIndicator();
    public void hideLoadMoreIndicator();
    public void showInfoMessage();
    public void showErrorMessage();
    public void showToolbar();
    public void hideToolbar();

    public DialogUtil getDialogUtil();
    public PostRecyclerView getRecyclerView();
}
