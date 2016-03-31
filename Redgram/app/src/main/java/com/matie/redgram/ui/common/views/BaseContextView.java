package com.matie.redgram.ui.common.views;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;

/**
 * Created by matie on 12/04/15.
 */
public interface BaseContextView extends BaseView {
     Context getContext();
     BaseActivity getBaseActivity();
     BaseFragment getBaseFragment();
}
