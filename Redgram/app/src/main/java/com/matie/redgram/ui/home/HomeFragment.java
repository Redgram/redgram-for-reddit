package com.matie.redgram.ui.home;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.base.SlidingUpPanelFragment;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.posts.LinksComponent;
import com.matie.redgram.ui.posts.LinksContainerView;
import com.matie.redgram.ui.posts.LinksModule;
import com.matie.redgram.ui.subcription.SubscriptionActivity;
import com.matie.redgram.ui.thread.views.CommentsActivity;
import com.nineoldandroids.view.ViewHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 17/01/15.
 */
public class HomeFragment extends SlidingUpPanelFragment implements HomeView,
        SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout homeSwipeContainer;
    @InjectView(R.id.links_container_view)
    LinksContainerView linksContainerView;

    Toolbar mToolbar;
    View mContentView;
    LayoutInflater mInflater;
    LinearLayoutManager mLayoutManager;

    PostRecyclerView homeRecyclerView;
    FrameLayout frameLayout;
    LinearLayout titleWrapper;
    TextView toolbarTitle;
    TextView toolbarSubtitle;
    ImageView listingFilter;
    ImageView listingRefresh;


    HomeComponent component;
    LinksComponent linksComponent;

    @Inject
    App app;
    @Inject
    HomePresenterImpl homePresenter;
    @Inject
    DialogUtil dialogUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.inject(this, view);

        setUpRecyclerView();

        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mContentView = getActivity().findViewById(R.id.container);
        mInflater = inflater;

        setupSwipeContainer();

        return view;
    }

    private void setUpRecyclerView() {
        homeRecyclerView = linksContainerView.getContainerRecyclerView();
        mLayoutManager = (LinearLayoutManager)homeRecyclerView.getLayoutManager();

        //this listener is responsible for invoking SWIPE-TO-REFRESH if the first item is fully visible.
        homeRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                boolean enable = false;
                if (homeRecyclerView != null && homeRecyclerView.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible;
                }
                homeSwipeContainer.setEnabled(enable);
            }
        });

    }

    //initial call to get data
    private void checkArgumentsAndUpdate() {
        String filter = getResources().getString(R.string.default_filter);
        if(getArguments() != null && getArguments().containsKey(SubscriptionActivity.RESULT_SUBREDDIT_NAME)){
            String subredditChoice = getArguments().getString(SubscriptionActivity.RESULT_SUBREDDIT_NAME);
            Map<String, String> params = new HashMap<>();
            linksContainerView.refreshView(subredditChoice, filter.toLowerCase(), params);
            toolbarTitle.setText(subredditChoice);
            toolbarSubtitle.setText(filter);
        } else{
            homePresenter.getHomeViewWrapper();
            linksContainerView.refreshView();
            toolbarSubtitle.setText(filter);
        }
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        MainComponent mainComponent = (MainComponent)appComponent;
        LinksModule linksModule = new LinksModule(linksContainerView, this);
        component = DaggerHomeComponent.builder()
                .mainComponent(mainComponent)
                .homeModule(new HomeModule(this))
                .linksModule(linksModule)
                .build();
        component.inject(this);
        linksComponent = component.getLinksComponent(linksModule);
        linksContainerView.setComponent(linksComponent);
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

        toolbarTitle = (TextView) rl.findViewById(R.id.home_toolbar_title);
        toolbarSubtitle = (TextView) rl.findViewById(R.id.home_toolbar_subtitle);
        listingFilter = (ImageView)rl.findViewById(R.id.listing_filter);
        listingRefresh = (ImageView)rl.findViewById(R.id.listing_refresh);

        titleWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeRecyclerView.getVisibility() == View.VISIBLE && homePresenter.getSubreddits() != null){

                    List<String> subreddits = homePresenter.getSubreddits();
                    //if a subreddit was selected before, add an option to return to home
                    if(!getResources().getString(R.string.frontpage).equalsIgnoreCase(toolbarTitle.getText().toString())){
                        subreddits.add(0, "Return to Frontpage");
                    }else{
                        if(!subreddits.isEmpty()){
                            subreddits.remove(0);
                        }
                    }

                    dialogUtil.build()
                            .title("Subreddits")
                            .items(subreddits.toArray(new CharSequence[subreddits.size()]))
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                                    String subredditChoice = charSequence.toString();
                                    String filterChoice = getContext().getResources().getString(R.string.default_filter);
                                    Map<String, String> params = new HashMap<String, String>();
                                    //common
                                    toolbarSubtitle.setText(filterChoice);

                                    if (i == 0 && "Return to Frontpage".equalsIgnoreCase(charSequence.toString())) {
                                        toolbarTitle.setText(getResources().getString(R.string.frontpage));
                                        linksContainerView.refreshView(null, filterChoice.toLowerCase(), params);
                                    } else {
                                        toolbarTitle.setText(subredditChoice);
                                        linksContainerView.refreshView(subredditChoice, filterChoice.toLowerCase(), params);
                                    }

                                }
                            })
                            .show();
                }
            }
        });

        toolbarTitle.setText(getResources().getString(R.string.frontpage));

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
                                            String filterChoice = charSequence.toString();
                                            linksContainerView.sortView(filterChoice.toLowerCase(), null);
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
                if (!homeSwipeContainer.isRefreshing()) {
                    linksContainerView.refreshView();
                }
            }
        });
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
            homeSwipeContainer.setProgressViewOffset(false, 0 , 50);
        }
    }


    private void callSortDialog(CharSequence query) {
        dialogUtil.build()
                .title("Sort By")
                .items(R.array.fromArray)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        if(!homeSwipeContainer.isRefreshing()){

                            //create parameters list for the network call
                            Map<String, String> params = new HashMap<>();
                            params.put("t", charSequence.toString().toLowerCase());

                            //perform network call
                            linksContainerView.sortView(query.toString().toLowerCase(), params);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CommentsActivity.REQ_CODE){
            if(resultCode == getActivity().RESULT_OK){
                PostItem postItem = new Gson().fromJson(data.getStringExtra(CommentsActivity.RESULT_POST_CHANGE), PostItem.class);
                int pos = data.getIntExtra(CommentsActivity.RESULT_POST_POS, -1);
                if(linksContainerView.getItems().contains(postItem) && pos >= 0){
                    linksContainerView.getItems().remove(pos);
                    linksContainerView.getItems().add(pos, postItem);
                    linksContainerView.refreshView();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        homePresenter.registerForEvents();
        linksContainerView.getLinksPresenter().registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        homePresenter.unregisterForEvents();
        linksContainerView.getLinksPresenter().unregisterForEvents();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeRecyclerView.clearOnScrollListeners();
        ButterKnife.reset(this);
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
    public void showInfoMessage() {}

    @Override
    public void showErrorMessage(String errorMsg) {
        dialogUtil.build().title("Error Message").content(errorMsg).show();
    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void hideToolbar() {

    }


    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity)getActivity();
    }

    @Override
    public BaseFragment getBaseFragment() {
        return this;
    }


    @Override
    public void onRefresh() {
        linksContainerView.refreshView();
    }

    @Override
    public void showPanel() {
        ((SlidingUpPanelActivity)getActivity()).showPanel();
    }

    @Override
    public void hidePanel() {
        ((SlidingUpPanelActivity)getActivity()).hidePanel();
    }

    @Override
    public void togglePanel() {
        ((SlidingUpPanelActivity)getActivity()).togglePanel();
    }

    @Override
    public void setPanelHeight(int height) {
        ((SlidingUpPanelActivity)getActivity()).setPanelHeight(height);
    }

    @Override
    public void setPanelView(Fragments fragmentEnum, Bundle bundle) {
        ((SlidingUpPanelActivity)getActivity()).setPanelView(fragmentEnum, bundle);
    }

    @Override
    public void setDraggable(View view) {
        //no implementation
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return ((SlidingUpPanelActivity) getActivity()).getPanelState();
    }

    public SwipeRefreshLayout getHomeSwipeContainer() {
        return homeSwipeContainer;
    }
}
