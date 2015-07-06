package com.matie.redgram.ui.search;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    @InjectView(R.id.progress_bar)
    ProgressBar progressBar;
    @InjectView(R.id.search_linear_layout)
    LinearLayout searchLinearLayout;
    @InjectView(R.id.search_recycler_view)
    PostRecyclerView searchRecyclerView;

    Toolbar mToolbar;
    View mContentView;
    LayoutInflater mInflater;

    SearchComponent component;
    @Inject
    SearchPresenterImpl searchPresenter;

    FrameLayout frameLayout;
    EditText searchView;
    ImageView searchClear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.inject(this,view);

        mContentView = getActivity().findViewById(R.id.container);
        mInflater = inflater;

        searchRecyclerView.setScrollViewCallbacks(this);

        return view;

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    protected void setupToolbar() {
        mToolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        frameLayout = (FrameLayout)mToolbar.findViewById(R.id.toolbar_child_view);

        LinearLayout ll = (LinearLayout) mInflater.inflate(R.layout.fragment_search_toolbar, frameLayout, false);
        frameLayout.removeAllViews();
        frameLayout.addView(ll);

        searchView = (EditText)ll.findViewById(R.id.search_view);
        searchClear = (ImageView)ll.findViewById(R.id.search_clear);

        //show keyboard on fragment enter
        toggleKeyboard(searchView, true);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String query = v.getText().toString();
                if(query.length() > 0){
                    searchPresenter.executeSearch(query);
                    searchView.setCursorVisible(false);
                }

                return false;
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0)
                    searchClear.setVisibility(View.VISIBLE);
                else
                    searchClear.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setText("");
                searchView.setHint("Search");
                searchView.setCursorVisible(true);
                toggleKeyboard(searchView, true);
            }
        });

    }

    private void toggleKeyboard(View focusedView, boolean show) {
        //you can use focusedView instead of getCurrentFocus() in the same way
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(show)
            inputMethodManager.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        else
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchPresenter.registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();
        //hide keyboard
        toggleKeyboard(searchView, false);

        searchPresenter.unregisterForEvents();
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
        searchRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);
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
