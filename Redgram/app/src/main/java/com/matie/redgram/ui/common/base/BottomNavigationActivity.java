package com.matie.redgram.ui.common.base;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.matie.redgram.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class BottomNavigationActivity extends BaseActivity {
    @InjectView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.container)
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            checkIntent();
        }
        setupBottomNavigation();
        setupToolbar();
    }


    protected abstract void checkIntent();

    protected void setupBottomNavigation() {

    }

    private void setupToolbar() {

    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
}
