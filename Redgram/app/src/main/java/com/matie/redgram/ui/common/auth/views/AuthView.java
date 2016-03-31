package com.matie.redgram.ui.common.auth.views;

import com.matie.redgram.ui.common.views.BaseContextView;

/**
 * Created by matie on 2016-02-21.
 */
public interface AuthView extends BaseContextView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
    void transitionToMainActivity();
}
