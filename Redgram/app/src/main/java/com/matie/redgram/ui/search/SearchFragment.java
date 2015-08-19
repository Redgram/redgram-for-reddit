package com.matie.redgram.ui.search;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SearchPresenterImpl;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.DialogUtil;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.search.views.SearchView;
import com.nineoldandroids.view.ViewHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 28/06/15.
 * todo: wrap layout with SwipeRefreshLayout and DISABLE functionality, but use loading animation.
 */
public class SearchFragment extends BaseFragment implements SearchView, ObservableScrollViewCallbacks{

    @InjectView(R.id.search_linear_layout)
    LinearLayout searchLinearLayout;
    @InjectView(R.id.search_recycler_view)
    PostRecyclerView searchRecyclerView;
    @InjectView(R.id.search_swipe_container)
    SwipeRefreshLayout searchSwipeContainer;

    Toolbar mToolbar;
    View mContentView;
    LayoutInflater mInflater;
    LinearLayoutManager mLayoutManager;

    SearchComponent component;

    @Inject
    SearchPresenterImpl searchPresenter;

    FrameLayout frameLayout;
    EditText searchView;
    ImageView searchClear;

    ImageView searchFilter;
    Spinner sortSpinner;
    Spinner fromSpinner;
    EditText limitToView;
    RelativeLayout filterContentLayout;

    List<String> sortArray;
    List<String> fromArray;
    Map<String, String> params;

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

        mLayoutManager = (LinearLayoutManager)searchRecyclerView.getLayoutManager();
        mContentView = getActivity().findViewById(R.id.container);
        mInflater = inflater;

        sortArray = Arrays.asList(getContext().getResources().getStringArray(R.array.searchSortArray));
        fromArray = Arrays.asList(getContext().getResources().getStringArray(R.array.fromArray));
        params = new HashMap<>();

        setupSwipeContainer();
        setupRecyclerView();

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
        searchFilter = (ImageView)ll.findViewById(R.id.search_filter);

        searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
                    return false;
                } else if (actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    //clear params
                    params.clear();
                    String query = v.getText().toString();
                    if (query.length() > 0) {
                        params.put("q", query);
                        searchPresenter.executeSearch("", params);
                        searchView.setCursorVisible(false);
                    }
                }
                //hide keyboard
                toggleKeyboard(false);
                return true;
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0)
                    searchClear.setVisibility(View.VISIBLE);
                else
                    searchClear.setVisibility(View.GONE);
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
                toggleKeyboard(true);
            }
        });

        setupFilterContentLayout();

        searchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialogUtil().init();

                //remove layout from its parent, to set it to another, new parent
                if (filterContentLayout.getParent() != null) {
                    ((ViewGroup) filterContentLayout.getParent()).removeView(filterContentLayout);
                }

                try {
                    getDialogUtil().getDialogBuilder()
                            .title("Advanced Search")
                            .customView(filterContentLayout, false)
                            .positiveText("Search")
                            .negativeText("Cancel")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    performPositiveEvent(dialog);
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                }
                            })
                            .show();
                } catch (NullPointerException e) {
                    Log.d("DIALOG", "Make sure you are initializing the builder.");
                }

            }
        });

        //focus on edit text and show keyboard
        searchView.setFocusable(true);
        searchView.setCursorVisible(true);
        toggleKeyboard(true);
    }

    private void setupSwipeContainer(){
        //disable it for search fragment
        searchSwipeContainer.setEnabled(false);
        searchSwipeContainer.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        TypedValue tv = new TypedValue();
        if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    getContext().getResources().getDisplayMetrics());
            //push it down to the same position as the first item to be loaded
            searchSwipeContainer.setProgressViewOffset(false, 0 , actionBarHeight+50);
        }
    }

    private void setupRecyclerView() {
        searchRecyclerView.setScrollViewCallbacks(this);
    }

    private void setupFilterContentLayout() {
        filterContentLayout = (RelativeLayout)mInflater.inflate(R.layout.fragment_search_toolbar_filter, null);
        fromSpinner = (Spinner)filterContentLayout.findViewById(R.id.spinner_from);
        sortSpinner = (Spinner)filterContentLayout.findViewById(R.id.spinner_sort);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, fromArray);
        fromAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, sortArray);
        sortAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);
        sortSpinner.setAdapter(sortAdapter);

        limitToView = (EditText)filterContentLayout.findViewById(R.id.limit_view);
    }

    private void performPositiveEvent(MaterialDialog dialog) {
        RelativeLayout layout = (RelativeLayout) dialog.getCustomView();
        EditText editText = (EditText) layout.findViewById(R.id.filter_query);

        String query = editText.getText().toString();
        String limitTo = limitToView.getText().toString();

        params.clear();
        params.put("q", (query.trim()));
        params.put("t", fromSpinner.getSelectedItem().toString());
        params.put("sort", sortSpinner.getSelectedItem().toString());

        searchPresenter.executeSearch(limitTo.trim(), params);

        if (!searchView.getText().toString().equals(query)) {
            searchView.setText(query);
        }
    }

    private void toggleKeyboard(boolean show) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (show)
            inputMethodManager.showSoftInput(getActivity().getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
        else {
            View focusedView = getActivity().getCurrentFocus();
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
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
        toggleKeyboard(false);
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
        searchSwipeContainer.setRefreshing(true);
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        //progressBar.setVisibility(View.GONE);
        searchSwipeContainer.setRefreshing(false);
        searchRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public DialogUtil getDialogUtil() {
        return ((MainActivity)getActivity()).getDialogUtil();
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
