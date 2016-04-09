package com.matie.redgram.ui.subcription;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SubscriptionPresenterImpl;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditRecyclerView;
import com.matie.redgram.ui.common.views.widgets.subreddit.SubredditViewHolder;
import com.matie.redgram.ui.subcription.views.SubscriptionView;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 2015-11-26.
 */
public class SubscriptionFragment extends BaseFragment implements SubscriptionView, SubredditViewHolder.SubredditViewHolderListener{

    @InjectView(R.id.subreddit_recycler_view)
    SubredditRecyclerView subredditRecyclerView;

    @Inject
    SubscriptionPresenterImpl subscriptionPresenter;

    SubscriptionComponent component;

    SubscriptionActivity activity;
    ToastHandler toastHandler;

    Toolbar mToolbar;
    LayoutInflater mInflater;

    FrameLayout frameLayout;
    ImageView subRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub, container, false);
        ButterKnife.inject(this, view);

        activity = (SubscriptionActivity) getActivity();
        toastHandler = activity.getApp().getToastHandler();

        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        mInflater = inflater;

        subredditRecyclerView.setAdapterListener(this);

        return view;
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        SubscriptionActivityComponent subscriptionActivityComponent = (SubscriptionActivityComponent)appComponent;
        component = DaggerSubscriptionComponent.builder()
                .subscriptionActivityComponent(subscriptionActivityComponent)
                .subscriptionModule(new SubscriptionModule(this))
                .build();

        component.inject(this);
    }

    @Override
    protected void setupToolbar() {
        //add refresh
        //setting up toolbar
        frameLayout = (FrameLayout)mToolbar.findViewById(R.id.toolbar_child_view);
        frameLayout.removeAllViews();

        RelativeLayout rl = (RelativeLayout) mInflater.inflate(R.layout.fragment_sub_toolbar, frameLayout, false);
        frameLayout.addView(rl);

        subRefresh = (ImageView)rl.findViewById(R.id.sub_refresh);

        setupRefresh();
    }

    private void setupRefresh() {
        subRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subredditRecyclerView.getVisibility() == View.VISIBLE){
                    //force load from network
                    // TODO: 2015-12-10 only call network call a minute after the last call was made
                    subscriptionPresenter.getSubreddits(true);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscriptionPresenter.getSubreddits(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        subscriptionPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        subscriptionPresenter.unregisterForEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
    public void showLoading() {
        subredditRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        subredditRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoMessage() {
    }

    @Override
    public void showErrorMessage(String error) {

    }

    @Override
    public SubredditRecyclerView getRecyclerView() {
        return subredditRecyclerView;
    }

    @Override
    public void onClick(String subredditName) {
        toastHandler.showToast(subredditName, Toast.LENGTH_SHORT);
        activity.closeActivityWithResult(subredditName);
    }

    @Override
    public void onLongClick(SubredditItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("name", item.getName());
        bundle.putString("description", item.getDescriptionHtml());
        bundle.putString("subreddit_type", item.getSubredditType());
        bundle.putString("submission_type", item.getSubmissionType());
        bundle.putLong("subscribers_count", item.getSubscribersCount());
        bundle.putInt("accounts_active", item.getAccountActive());
        activity.openDetailsFragment(bundle);
    }
}
