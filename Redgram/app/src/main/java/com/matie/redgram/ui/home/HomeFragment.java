package com.matie.redgram.ui.home;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.home.views.widgets.postlist.PostRecyclerView;
import com.nineoldandroids.view.ViewHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TextView toolbarSubtitle;git 
    ImageView listingFilter;

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

        RelativeLayout rl = (RelativeLayout) mInflater.inflate(R.layout.fragment_home_toolbar, frameLayout, false);
        frameLayout.addView(rl);

        toolbarTitle = (TextView)rl.findViewById(R.id.home_toolbar_title);
        toolbarSubtitle = (TextView)rl.findViewById(R.id.home_toolbar_subtitle);
        listingFilter = (ImageView)rl.findViewById(R.id.listing_filter);

        toolbarTitle.setText("Frontpage");

        listingFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogUtil().init();

                try {
                    getDialogUtil().getDialogBuilder()
                            .title("Filter Links By")
                            .items(R.array.frontArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                    if (i == 3 || i == 4) {
                                        callSortDialog(charSequence);
                                    } else {
                                        homePresenter.getListing(charSequence.toString().toLowerCase(), null);
                                        toolbarSubtitle.setText(charSequence.toString());
                                    }
                                }
                            })
                            .show();
                }catch (NullPointerException e){
                    Log.d("DIALOG", "Make sure you are initializing the builder.");
                }
            }
        });
    }

    private void callSortDialog(CharSequence query) {
        getDialogUtil().init();
        getDialogUtil().getDialogBuilder()
                .title("Sort By")
                .items(R.array.fromArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        //create parameters list for the network call
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("t", charSequence.toString().toLowerCase());
                        //perform network call
                        homePresenter.getListing(query.toString().toLowerCase(), params);
                        //change subtitle only
                        String bullet = getContext().getResources().getString(R.string.text_bullet);
                        toolbarSubtitle.setText(query+" "+bullet+" "+charSequence);
                    }
                }).show();
    }

//    @Override
//    public HomeComponent component() {
//        return component;
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        String query = "Hot";
        homePresenter.getListing(query.toLowerCase(), null);
        toolbarSubtitle.setText(query);
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
    public DialogUtil getDialogUtil() {
        return ((MainActivity)getActivity()).getDialogUtil();
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
