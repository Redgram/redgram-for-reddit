package com.matie.redgram.ui.common.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.matie.redgram.R;
import com.matie.redgram.ui.common.views.adapters.SectionsPagerAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2016-08-22.
 */
public abstract class ViewPagerActivity extends BaseActivity {

    //any activity extending this class must have the following layouts defined in the activity XML.
    @InjectView(R.id.app_bar)
    AppBarLayout appBarLayout;
    @InjectView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @InjectView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.container)
    ViewPager mViewPager;

    private SectionsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        if(getIntent() != null){
            checkIntent();
        }
        setupViewPager();
        setupToolbar();
        setToolbarTitle(getInitialPagerPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    protected abstract int getInitialPagerPosition();
    protected abstract SectionsPagerAdapter pagerAdapterInstance();
    protected abstract void checkIntent();

    public ViewPager getViewPager(){
        return mViewPager;
    }

    public SectionsPagerAdapter getPagerAdapter(){
        return pagerAdapter;
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return collapsingToolbarLayout;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected void setupViewPager(){
        pagerAdapter = pagerAdapterInstance();
        mViewPager.setAdapter(pagerAdapter);
    }

    protected void setToolbarTitle(int position) {
        if(getSupportActionBar() != null && getPagerAdapter() != null){
            getSupportActionBar().setTitle(getPagerAdapter().getPageTitle(position));
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void setupToolbar() {
        setSupportActionBar(getToolbar());
        if(getSupportActionBar() != null){
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setShowHideAnimationEnabled(true);
        }
    }

}
