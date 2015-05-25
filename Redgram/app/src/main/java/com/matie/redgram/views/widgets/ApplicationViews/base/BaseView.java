package com.matie.redgram.views.widgets.ApplicationViews.base;

import android.support.v4.app.Fragment;

/**
 * Created by matie on 12/04/15.
 *
 * All views must inherit from this class. Views extending this class
 * share same use cases.
 */
public interface BaseView {
    public Fragment getFragment();
}
