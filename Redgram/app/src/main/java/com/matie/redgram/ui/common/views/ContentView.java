package com.matie.redgram.ui.common.views;

import android.content.Intent;
import android.os.Bundle;

import com.matie.redgram.ui.common.base.Fragments;

/**
 * Created by matie on 2016-03-20.
 */
public interface ContentView extends BaseContextView {
    void showLoading();
    void hideLoading();
    void showInfoMessage();
    void showErrorMessage(String error);
}
