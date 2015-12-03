package com.matie.redgram.ui.home;


import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.subcription.SubscriptionActivity;
import com.nineoldandroids.view.ViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 17/01/15.
 */
public class HomeFragment extends SlidingUpPanelFragment implements HomeView, ObservableScrollViewCallbacks,
        SwipeRefreshLayout.OnRefreshListener{

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
    LinearLayout titleWrapper;
    TextView toolbarTitle;
    TextView toolbarSubtitle;
    ImageView listingFilter;
    ImageView listingRefresh;

    String subredditChoice;
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

        // TODO: 2015-10-27 include in settings as values to set
        subredditChoice = null;
        filterChoice = getContext().getResources().getString(R.string.default_home_filter); //default value
        params = new HashMap<String,String>();

        setupSwipeContainer();
        setupListeners();
        setupRecyclerView();

        return view;
    }

    private void checkArgumentsAndUpdate() {
        if(getArguments() != null && getArguments().containsKey(SubscriptionActivity.RESULT_SUBREDDIT_NAME)){
            subredditChoice = getArguments().getString(SubscriptionActivity.RESULT_SUBREDDIT_NAME);
            homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
            toolbarTitle.setText(subredditChoice);
        } else{
            homePresenter.getHomeViewWrapper();
        }
        // TODO: 2015-10-27 set the title after the data is retrieved
        toolbarSubtitle.setText(filterChoice);
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        MainComponent mainComponent = (MainComponent)appComponent;
        component = DaggerHomeComponent.builder()
                .mainComponent(mainComponent)
                .homeModule(new HomeModule(this))
                .build();

        component.inject(this);
    }
    @Override
    protected void setupToolbar() {
        //setting up toolbar
        frameLayout = (FrameLayout)mToolbar.findViewById(R.id.toolbar_child_view);
        frameLayout.removeAllViews();

        RelativeLayout rl = (RelativeLayout) mInflater.inflate(R.layout.fragment_home_toolbar, frameLayout, false);
        frameLayout.addView(rl);

        titleWrapper = (LinearLayout)rl.findViewById(R.id.home_toolbar_title_linear_layout);
        //// TODO: 2015-10-20 handle subreddits and change the view to indicate a clickable

        toolbarTitle = (TextView)rl.findViewById(R.id.home_toolbar_title);
        toolbarSubtitle = (TextView)rl.findViewById(R.id.home_toolbar_subtitle);
        listingFilter = (ImageView)rl.findViewById(R.id.listing_filter);
        listingRefresh = (ImageView)rl.findViewById(R.id.listing_refresh);

        titleWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeRecyclerView.getVisibility() == View.VISIBLE && homePresenter.getSubreddits() != null){

                    List<String> subreddits = homePresenter.getSubreddits();

                    //if a subreddit was selected before, add an option to return to home
                    if(subredditChoice != null && !subredditChoice.isEmpty()){
                        subreddits.add(0, "Return to Frontpage");
                    }else{
                        if("Frontpage".equalsIgnoreCase(subreddits.get(0))){
                            subreddits.remove(0);
                        }
                    }

                    dialogUtil.build()
                            .title("Subreddits")
                            .items(subreddits.toArray(new CharSequence[subreddits.size()]))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                                    if (i == 0 && "Return to Frontpage".equalsIgnoreCase(charSequence.toString())) {
                                        homePresenter.getHomeViewWrapper();
                                        toolbarTitle.setText("Frontpage");
                                        filterChoice = getContext().getResources().getString(R.string.default_home_filter);
                                        toolbarSubtitle.setText(filterChoice);
                                        subredditChoice = null;
                                    } else {
                                        subredditChoice = charSequence.toString();
                                        toolbarTitle.setText(subredditChoice);
                                        homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
                                    }
                                }
                            })
                            .show();
                }
            }
        });

        toolbarTitle.setText("Frontpage");

        listingFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dialogUtil.build()
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
                                            homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
                                            toolbarSubtitle.setText(filterChoice);
                                        }
                                    }
                                }
                            })
                            .show();

            }
        });

        listingRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!homeSwipeContainer.isRefreshing()){
                    homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
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
                        int lastItemPosition = homeRecyclerView.getAdapter().getItemCount() - 1;
                        if(mLayoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
                            homePresenter.getMoreListing(subredditChoice, filterChoice.toLowerCase(), params);
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

        dialogUtil.build()
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
                            homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
                            //change subtitle only
                            String bullet = getContext().getResources().getString(R.string.text_bullet);
                            toolbarSubtitle.setText(query+" "+bullet+" "+charSequence);
                        }
                    }
                }).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        checkArgumentsAndUpdate();
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
    public void showInfoMessage() {}

    @Override
    public void showErrorMessage(String errorMsg) {
//        App app = (App)(getActivity().getApplication());
//        ToastHandler toastHandler = app.getToastHandler();
//        toastHandler.showToast(errorMsg, Toast.LENGTH_SHORT);

        dialogUtil.build().title("Error Message").content(errorMsg).show();
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

    // TODO: 2015-10-27 make sure to update the params list IF there was anything set up in sharedPreferences for params.
    // TODO: 2015-10-27 make sure to update the filterChoice when loading the presenter and found a filterChoice.
    @Override
    public void onRefresh() {
        homePresenter.getListing(subredditChoice, filterChoice.toLowerCase(), params);
    }

    @Override
    public void showPanel() {
        ((MainActivity)getActivity()).showPanel();
    }

    @Override
    public void hidePanel() {
        ((MainActivity)getActivity()).hidePanel();
    }

    @Override
    public void togglePanel() {
        ((MainActivity)getActivity()).togglePanel();
    }

    @Override
    public void setPanelHeight(int height) {
        ((MainActivity)getActivity()).setPanelHeight(height);
    }

    @Override
    public void setPanelView(Fragments fragmentEnum, Bundle bundle) {
//        ((MainActivity)getActivity()).setPanelView(fragmentName);
    }

    @Override
    public void setDragable(View view) {
        //no implementation
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return ((MainActivity)getActivity()).getPanelState();
    }
}
