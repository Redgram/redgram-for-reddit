package com.matie.redgram.ui.common.views;

import android.content.Context;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by matie on 12/04/15.
 */
public interface BaseContextView extends BaseView {
     Context getContext();
     RxAppCompatActivity getBaseActivity();
     RxFragment getBaseFragment();
}
