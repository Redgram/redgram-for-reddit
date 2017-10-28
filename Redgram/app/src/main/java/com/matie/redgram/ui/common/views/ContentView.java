package com.matie.redgram.ui.common.views;

/**
 * Created by matie on 2016-03-20.
 */
public interface ContentView extends BaseView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
    BaseView getParentView();
}
