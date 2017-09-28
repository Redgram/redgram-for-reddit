package com.matie.redgram.ui.common.base;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.utils.display.CustomFragmentManager;

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
    @InjectView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private CustomFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        if (getIntent() != null) {
            checkIntent();
        }
        setupBottomNavigation();
        setupToolbar();
        setupFragmentManager();
    }

    private void setupFragmentManager() {
        fragmentManager = new CustomFragmentManager();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentManager, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(fragmentManager, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(fragmentManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    protected abstract void checkIntent();

    protected void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> true
        );

        bottomNavigationView.setOnNavigationItemReselectedListener(
                item -> {

                }
        );
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
}
