package com.matie.redgram.ui.home;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseComponent;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.home.views.widgets.postlist.PostRecyclerView;
import com.nineoldandroids.view.ViewHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 17/01/15.
 */
public class HomeFragment extends BaseFragment implements HomeView, ObservableScrollViewCallbacks{

    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.home_linear_layout)
    LinearLayout homeLinearLayout;
    @InjectView(R.id.home_recycler_view)
    PostRecyclerView homeRecyclerView;

    Toolbar mToolbar;
    View mContentView;
    LayoutInflater mInflater;
    LinearLayoutManager mLayoutManager;

    FrameLayout frameLayout;
    TextView toolbarTitle;

    HomeComponent component;

    @Inject
    HomePresenterImpl homePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        homeRecyclerView.setScrollViewCallbacks(this);

        mLayoutManager = (LinearLayoutManager)homeRecyclerView.getLayoutManager();
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mContentView = getActivity().findViewById(R.id.container);
        mInflater = inflater;

        return view;
    }


    @Override

    protected void setupComponent(AppComponent appComponent) {
        MainComponent mainComponent = (MainComponent)appComponent;
        component = DaggerHomeComponent.builder()
                    .mainComponent(mainComponent)
                    .homeModule(new HomeModule(this))
                    .build();
        //component.inject(this);

        //todo: find another way to use injected instances
        homePresenter = (HomePresenterImpl)component.getHomePresenter();
    }

    @Override
    protected void setupToolbar() {
        //setting up toolbar
        frameLayout = (FrameLayout)mToolbar.findViewById(R.id.toolbar_child_view);
        frameLayout.removeAllViews();

        LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.fragment_home_toolbar, frameLayout, false);
        frameLayout.addView(ll);

        toolbarTitle = (TextView)ll.findViewById(R.id.home_toolbar_title);

        MainActivity mainActivity = ((MainActivity)getActivity());

        toolbarTitle.setText(mainActivity.getNavigationItems().get(mainActivity.getCurrentSelectedPosition()).getItemName());
    }

//    @Override
//    public HomeComponent component() {
//        return component;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //todo: call in a separate method
        homePresenter.populateView();
    }

    @Override
    public void onResume() {
        super.onResume();
        homePresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        homePresenter.unregisterForEvents();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
               hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    @Override
    public void showProgress() {
        homeRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        homeRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage() {

    }


    @Override
    public void showToolbar() {
        moveToolbar(0);
    }

    @Override
    public void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    @Override
    public PostRecyclerView getRecyclerView() {
        return homeRecyclerView;
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    public boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    public boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY((View) homeRecyclerView, translationY);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((View) homeRecyclerView).getLayoutParams();
                lp.height = (int) -translationY + mContentView.getHeight() - lp.topMargin;
                ((View) homeRecyclerView).requestLayout();
            }
        });
        animator.start();
    }

}
