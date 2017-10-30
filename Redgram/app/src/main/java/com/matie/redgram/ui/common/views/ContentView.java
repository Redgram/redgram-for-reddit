package com.matie.redgram.ui.common.views;


public interface ContentView extends BaseView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
}
