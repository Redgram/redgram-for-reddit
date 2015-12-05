package com.matie.redgram.ui.subcription;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SubscriptionPresenterImpl;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.ToastHandler;
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

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        subscriptionPresenter.getSubreddits();
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
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void showLoading() {
        //// TODO: 2015-11-30  
    }

    @Override
    public void hideLoading() {
        //// TODO: 2015-11-30  
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
        bundle.putString("description", item.getDescription());
        bundle.putString("subreddit_type", item.getSubredditType());
        activity.openDetailsFragment(bundle);
    }
}
