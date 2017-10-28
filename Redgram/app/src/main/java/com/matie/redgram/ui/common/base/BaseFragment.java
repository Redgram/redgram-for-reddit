package com.matie.redgram.ui.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.views.BaseView;
import com.trello.rxlifecycle.components.support.RxFragment;

import icepick.Icepick;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseFragment extends RxFragment implements BaseView {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //all common tasks
        setupComponent();
        setup();
    }

    protected void setup() {
        setupToolbar();
    }

    public void openIntent(Intent intent, int enterAnim, int exitAnim){
        startActivity(intent);
        getActivity().overridePendingTransition(enterAnim, exitAnim);
    }

    public void openIntentForResult(Intent intent, int requestCode){
        openIntentForResult(intent, requestCode, R.anim.enter, R.anim.exit);
    }

    public void openIntentForResult(Intent intent, int requestCode, int enterAnim, int exitAnim){
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(enterAnim, exitAnim);
    }

    protected void setupToolbar() {}

    protected abstract void setupComponent();

    @Override
    public Context getContext() {
        return getBaseActivity().getContext();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

    @Override
    public BaseFragment getBaseFragment() {
        return this;
    }

}
