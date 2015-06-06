package com.matie.redgram.ui.common.views;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by matie on 12/04/15.
 */
public interface BaseContextView extends BaseView {
    public Context getContext();
    public Fragment getFragment();
}
