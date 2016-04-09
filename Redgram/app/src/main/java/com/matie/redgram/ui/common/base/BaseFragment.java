package com.matie.redgram.ui.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.matie.redgram.ui.posts.LinksContainerView;
import com.matie.redgram.ui.search.SearchFragment;
import com.matie.redgram.ui.thread.views.CommentsActivity;

import icepick.Icepick;

/**
 * Created by matie on 09/06/15.
 */
public abstract class BaseFragment extends Fragment {

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
        setupToolbar();
    }

    protected abstract void setupComponent();

    protected abstract void setupToolbar();

}
