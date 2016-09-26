package com.matie.redgram.ui.common.views;

import android.content.Context;

import com.google.gson.JsonElement;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import rx.Observable;

/**
 * Created by matie on 12/04/15.
 */
public interface BaseContextView extends BaseView {
     Context getContext();
     BaseActivity getBaseActivity();
     BaseFragment getBaseFragment();
}
