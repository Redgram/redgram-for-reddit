package com.matie.redgram.ui.common.views;

import android.view.View;

/**
 * Created by matie on 2016-09-15.
 */
public interface DrawerView extends BaseContextView {
    void modifyNavDrawer(View view, int colorId);
    void resetNavDrawer();
}
