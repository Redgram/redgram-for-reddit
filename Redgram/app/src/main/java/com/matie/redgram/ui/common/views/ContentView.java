package com.matie.redgram.ui.common.views;


import com.matie.redgram.ui.base.BaseView;

public interface ContentView extends BaseView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
}
