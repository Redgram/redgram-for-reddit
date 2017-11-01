package com.matie.redgram.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.HomePresenterImpl;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.base.SlidingUpPanelFragment;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.links.LinksComponent;
import com.matie.redgram.ui.links.LinksContainerView;
import com.matie.redgram.ui.links.LinksControlView;
import com.matie.redgram.ui.links.LinksModule;
import com.matie.redgram.ui.subcription.SubscriptionActivity;
import com.matie.redgram.ui.thread.ThreadActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeFragment extends SlidingUpPanelFragment implements HomeView,
        SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout homeSwipeContainer;
    @InjectView(R.id.links_container_view)
    LinksContainerView linksContainerView;

    LinearLayoutManager mLayoutManager;

    PostRecyclerView homeRecyclerView;
    LinksControlView linksControlView;

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
            setControllerTitle(subredditChoice);
            setControllerSubTitle(filter);
        } else{
            homePresenter.getHomeViewWrapper();
            setControllerSubTitle(filter);
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
        linksContainerView.setHostingFragmentTag(Fragments.HOME.toString());
    }
    @Override
    protected void setupToolbar() {
        ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setCustomView(R.layout.links_control_view);

            View controlView = supportActionBar.getCustomView();
            if (controlView instanceof LinksControlView) {
                linksControlView = (LinksControlView) controlView;

                setupToolbarTitle();
                setupToolbarSubredditPicker();
                setupToolbarFilter();
                setupToolbarRefresh();
            }
        }
    }

    private void setupToolbarRefresh() {
        if (linksControlView == null) return;
        linksControlView.setRefreshListener(v -> {
            if (!homeSwipeContainer.isRefreshing()) {
                linksContainerView.refreshView();
            }
        });
    }

    private void setupToolbarFilter() {
        if (linksControlView == null) return;
        linksControlView.setFilterListener(v -> dialogUtil.build()
                .title("Filter Links By")
                .items(R.array.frontArray)
                .itemsCallback((materialDialog, view, i, charSequence) -> {
                    //if top or controversial, call sort dialog
                    if (i == 3 || i == 4) {
                        callSortDialog(charSequence);
                    } else {
                        if(!homeSwipeContainer.isRefreshing()){
                            String filterChoice = charSequence.toString();
                            linksContainerView.sortView(filterChoice.toLowerCase(), null);
                            setControllerSubTitle(filterChoice);
                        }
                    }
                })
                .show()
        );

    }

    private void setupToolbarTitle() {
        if (linksControlView == null) return;
        linksControlView.setTitle(getResources().getString(R.string.frontpage));
    }

    private void setupToolbarSubredditPicker() {
        if (linksControlView == null) return;
        linksControlView.setItemPickerListener(v -> {
            List<String> subreddits = homePresenter.getSubreddits();
            if(homeRecyclerView.getVisibility() == View.VISIBLE && subreddits != null){
                if(userOnFrontPage()) {
                    if(!subreddits.isEmpty()){
                        subreddits.remove(0);
                    }
                } else {
                    //if a subreddit was selected before, add an option to return to home
                    subreddits.add(0, "Frontpage");
                }

                dialogUtil.build()
                        .title("Subreddit")
                        .items(subreddits.toArray(new CharSequence[subreddits.size()]))
                        .itemsCallback((materialDialog, view, i, charSequence) -> {

                            String subredditChoice = charSequence.toString();
                            String filterChoice = getContext().getResources().getString(R.string.default_filter);
                            Map<String, String> params = new HashMap<>();

                            //common
                            setControllerSubTitle(filterChoice);

                            if (i == 0 && "Return to Frontpage".equalsIgnoreCase(charSequence.toString())) {
                                setControllerTitle(getResources().getString(R.string.frontpage));
                                linksContainerView.refreshView(null, filterChoice.toLowerCase(), params);
                            } else {
                                setControllerTitle(subredditChoice);
                                linksContainerView.refreshView(subredditChoice, filterChoice.toLowerCase(), params);
                            }

                        })
                        .show();
            }
        });
    }

    private boolean userOnFrontPage() {
        final String frontPageResource = getResources().getString(R.string.frontpage);
        final String currentOption = linksControlView.getTitleView().getText().toString();
        return frontPageResource.equalsIgnoreCase(currentOption);
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
                .itemsCallback((materialDialog, view, i, charSequence) -> {
                    if(!homeSwipeContainer.isRefreshing()){

                        //create parameters list for the network call
                        Map<String, String> params = new HashMap<>();
                        params.put("t", charSequence.toString().toLowerCase());

                        //perform network call
                        linksContainerView.sortView(query.toString().toLowerCase(), params);

                        //change subtitle only
                        String bullet = getContext().getResources().getString(R.string.text_bullet);
                        setControllerSubTitle(query + " " + bullet + " " + charSequence);
                    }
                }).show();
    }


    private void setControllerTitle(String value) {
        if (linksControlView == null) return;
        linksControlView.setTitle(value);
    }

    private void setControllerSubTitle(String value) {
        if (linksControlView == null) return;
        linksControlView.setSubTitle(value);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        checkArgumentsAndUpdate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ThreadActivity.REQ_CODE){
            if(resultCode == Activity.RESULT_OK){
                PostItem postItem = new Gson()
                        .fromJson(data.getStringExtra(ThreadActivity.RESULT_POST_CHANGE), PostItem.class);
                int pos = data.getIntExtra(ThreadActivity.RESULT_POST_POS, -1);
                if(linksContainerView.getItems().contains(postItem) && pos >= 0){
                    // TODO: 2016-04-18 override hashcode to check whether item has actually changed before calling update
                    if(postItem.isHidden()){
                        linksContainerView.removeItem(pos);
                    }else{
                        linksContainerView.updateItem(pos, postItem);
                    }
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
    public void onDestroyView() {
        homeRecyclerView.clearOnScrollListeners();
        homePresenter.unregisterForEvents();
        linksContainerView.getLinksPresenter().unregisterForEvents();

        ButterKnife.reset(this);
        super.onDestroyView();
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
    public void loadLinksContainer(Listing<PostItem> links) {
        linksContainerView.setLoadMoreId(links.getAfter());
        linksContainerView.updateList(links.getItems());
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
