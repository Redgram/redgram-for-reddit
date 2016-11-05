package com.matie.redgram.ui.common.main.views;

import android.view.View;

import com.matie.redgram.ui.common.views.ContentView;

/**
 * Created by matie on 2016-09-25.
 */
public interface MainView extends ContentView {
    void modifyNavDrawer(View view, int colorId);
    void resetNavDrawer();
}
