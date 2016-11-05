package com.matie.redgram.ui.common.utils.display;

import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.matie.redgram.ui.common.views.ContentView;

/**
 * Created by matie on 2016-03-29.
 */
public interface CoordinatorLayoutInterface {
    CoordinatorLayout coordinatorLayout();
    void showSnackBar(String msg, int length, @Nullable String actionText, @Nullable View.OnClickListener onClickListener, @Nullable Snackbar.Callback callback);
}
