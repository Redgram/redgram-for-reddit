package com.matie.redgram.ui.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SearchPresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.home.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.search.views.SearchView;
import com.nineoldandroids.view.ViewHelper;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by matie on 28/06/15.
 */
public class SearchFragment extends BaseFragment implements SearchView, ObservableScrollViewCallbacks {

    @InjectView(R.id.search_view)
    EditText searchText;
    @InjectView(R.id.search_button)
    Button searchButton;
    @InjectView(R.id.search_recycler_view)
    PostRecyclerView searchRecyclerView;

    Toolbar mToolbar;
    View mContentView;

    SearchComponent component;
    @Inject
    SearchPresenterImpl searchPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this, view);

    return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setupComponent(AppComponent appComponent) {
        MainComponent mainComponent = (MainComponent)appComponent;
        component = DaggerSearchComponent.builder()
                    .mainComponent(mainComponent)
                    .searchModule(new SearchModule(this))
                    .build();
        searchPresenter = (SearchPresenterImpl)component.getSearchPresenter();
    }

    @OnClick(R.id.search_button)
    public void executeSearch(){
        String query = searchText.getText().toString();
        if(query.length() > 0){
            searchPresenter.executeSearch(query);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        searchPresenter.registerForEvents();
    }

    @Override
    public void onStop() {
        searchPresenter.unregisterForEvents();
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

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
    public void showToolbar() {
        moveToolbar(0);
    }

    @Override
    public void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
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
                ViewHelper.setTranslationY((View) searchRecyclerView, translationY);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((View) searchRecyclerView).getLayoutParams();
                lp.height = (int) -translationY + mContentView.getHeight() - lp.topMargin;
                ((View) searchRecyclerView).requestLayout();
            }
        });
        animator.start();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public PostRecyclerView getRecyclerView() {
        return searchRecyclerView;
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
