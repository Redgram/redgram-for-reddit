package com.matie.redgram.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupComponent(((BaseActivity)getActivity()).component());
    }

    protected abstract void setupComponent(BaseComponent component);

    protected abstract BaseComponent component();
}
