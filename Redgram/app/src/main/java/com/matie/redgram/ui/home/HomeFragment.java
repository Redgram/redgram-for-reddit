package com.matie.redgram.ui.home;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 17/01/15.
 */
public class HomeFragment extends BaseFragment implements HomeView, ObservableScrollViewCallbacks, SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.home_swipe_container)
    SwipeRefreshLayout homeSwipeContainer;
    @InjectView(R.id.home_linear_layout)
    LinearLayout homeLinearLayout;
    @InjectView(R.id.home_recycler_view)
    PostRecyclerView homeRecyclerView;
    @InjectView(R.id.home_load_more_bar)
    ProgressBar homeProgressBar;

    Toolbar mToolbar;
    View mContentView;
    LayoutInflater mInflater;
    LinearLayoutManager mLayoutManager;

    FrameLayout frameLayout;
    TextView toolbarTitle;
    TextView toolbarSubtitle;
    ImageView listingFilter;
    ImageView listingRefresh;

    String filterChoice;
    Map<String,String> params;

    //recycler view listeners to add.
    RecyclerView.OnScrollListener swipeLayoutEnablerListener;
    RecyclerView.OnScrollListener loadMoreListener;

    HomeComponent component;

    @Inject
    HomePresenterImpl homePresenter;
    @Inject
    DialogUtil dialogUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        mLayoutManager = (LinearLayoutManager)homeRecyclerView.getLayoutManager();
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mContentView = getActivity().findViewById(R.id.container);
        mInflater = inflater;

        filterChoice = "Hot"; //default value
        params = new HashMap<String,String>();

        setupSwipeContainer();
        setupListeners();
        setupRecyclerView();

        return view;
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        MainComponent mainComponent = (MainComponent)appComponent;
        component = DaggerHomeComponent.builder()
                .mainComponent(mainComponent)
                .homeModule(new HomeModule(this))
                .build();

        component.inject(this);

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
        listingRefresh = (ImageView)rl.findViewById(R.id.listing_refresh);

        toolbarTitle.setText("Frontpage");

        listingFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.init();

                try {
                    dialogUtil.getDialogBuilder()
                            .title("Filter Links By")
                            .items(R.array.frontArray)
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                    //if top or controversial, call sort dialog
                                    if (i == 3 || i == 4) {
                                        callSortDialog(charSequence);
                                    } else {
                                        if(!homeSwipeContainer.isRefreshing()){
                                            //necessary as not all filters have url parameters in their calls
                                            params.clear();
                                            //keep track of filter choice
                                            filterChoice = charSequence.toString();
                                            homePresenter.getListing(filterChoice.toLowerCase(), params);
                                            toolbarSubtitle.setText(filterChoice);
                                        }
                                    }
                                }
                            })
                            .show();
                } catch (NullPointerException e) {
                    Log.d("DIALOG", "Make sure you are initializing the builder.");
                }
            }
        });

        listingRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!homeSwipeContainer.isRefreshing()){
                    homePresenter.getListing(filterChoice.toLowerCase(), params);
                }
            }
        });
    }

    private void setupListeners() {

        //this listener is responsible for invoking SWIPE-TO-REFRESH if the first item is fully visible.
        swipeLayoutEnablerListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                boolean enable = false;
                if(homeRecyclerView != null && homeRecyclerView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible;
                }
                homeSwipeContainer.setEnabled(enable);
            }
        };

        //this listener is responsible for LOAD MORE. This listener object will be added on startup. Removed when
        // a network call is done and added again when the requested network call is either COMPLETED or had an ERROR.
        loadMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(homeRecyclerView != null && homeRecyclerView.getChildCount() > 0){
                        int lastItemPosition = homeRecyclerView.getPostAdapter().getItemCount() - 1;
                        if(mLayoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
                            homePresenter.getMoreListing(filterChoice.toLowerCase(), params);
                        }
                    }
                }
            }
        };

    }

    private void setupSwipeContainer(){
        homeSwipeContainer.setOnRefreshListener(this);

        homeSwipeContainer.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getContext().getResources().getDisplayMetrics());
            //push it down to the same position as the first item to be loaded
            homeSwipeContainer.setProgressViewOffset(false, 0 , actionBarHeight+50);
        }
    }

    private void setupRecyclerView(){
        homeRecyclerView.setScrollViewCallbacks(this);
        //enable swipe to refresh when the FIRST time is visible ONLY.
        homeRecyclerView.addOnScrollListener(swipeLayoutEnablerListener);
        homeRecyclerView.addOnScrollListener(loadMoreListener);
    }

    private void callSortDialog(CharSequence query)
    {
        filterChoice = query.toString();

        dialogUtil.init();
        dialogUtil.getDialogBuilder()
                .title("Sort By")
                .items(R.array.fromArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if(!homeSwipeContainer.isRefreshing()){
                            //create parameters list for the network call
                            Map<String, String> urlParams = new HashMap<String, String>();
                            urlParams.put("t", charSequence.toString().toLowerCase());
                            //keep track of filter sort choice
                            params = urlParams;
                            //perform network call
                            homePresenter.getListing(filterChoice.toLowerCase(), params);
                            //change subtitle only
                            String bullet = getContext().getResources().getString(R.string.text_bullet);
                            toolbarSubtitle.setText(query+" "+bullet+" "+charSequence);
                        }
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

        homePresenter.getListing(filterChoice.toLowerCase(), params);
        toolbarSubtitle.setText(filterChoice);
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
        homeRecyclerView.clearOnScrollListeners();
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
    public void showLoading() {
        homeRecyclerView.setVisibility(View.GONE);
        homeSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        homeSwipeContainer.setRefreshing(false);
        homeRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadMoreIndicator() {
        homeRecyclerView.removeOnScrollListener(loadMoreListener);
        homeProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadMoreIndicator() {
        homeRecyclerView.addOnScrollListener(loadMoreListener);
        homeProgressBar.setVisibility(View.GONE);
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


//    public DialogUtil dialogUtil {
//        return ((MainActivity)getActivity()).dialogUtil;
//    }

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

    @Override
    public void onRefresh() {
        homePresenter.getListing(filterChoice.toLowerCase() , params);
    }
}
