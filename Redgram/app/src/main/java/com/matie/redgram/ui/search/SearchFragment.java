package com.matie.redgram.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SearchPresenterImpl;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.AppComponent;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.base.SlidingUpPanelFragment;
import com.matie.redgram.ui.common.main.MainActivity;
import com.matie.redgram.ui.common.main.MainComponent;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.submission.links.LinksComponent;
import com.matie.redgram.ui.submission.links.LinksModule;
import com.matie.redgram.ui.search.views.SearchView;
import com.matie.redgram.ui.submission.links.delegates.LinksFeedDelegate;
import com.matie.redgram.ui.submission.links.views.LinksFeedLayout;
import com.matie.redgram.ui.submission.links.views.LinksView;
import com.matie.redgram.ui.thread.ThreadActivity;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFragment extends SlidingUpPanelFragment implements SearchView {

    @InjectView(R.id.links_container_view)
    LinksFeedLayout linksFeedLayout;
    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout searchSwipeContainer;

    View mContentView;
    LayoutInflater mInflater;

    SearchComponent component;
    LinksComponent linksComponent;

    EditText searchView;
    ImageView searchClear;

    ImageView searchFilter;
    Spinner sortSpinner;
    Spinner fromSpinner;
    EditText limitToView;
    RelativeLayout filterContentLayout;
    RelativeLayout customToolbar;

    List<String> sortArray;
    List<String> fromArray;

    private String subreddit;
    private Map<String, String> params = new HashMap<>();

    @Inject
    SearchPresenterImpl searchPresenter;
    @Inject
    DialogUtil dialogUtil;


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

        sortArray = Arrays.asList(getContext().getResources().getStringArray(R.array.searchSortArray));
        fromArray = Arrays.asList(getContext().getResources().getStringArray(R.array.fromArray));

        setupSwipeContainer();

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ThreadActivity.REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final LinksView linksView = linksComponent.getLinksView();

                PostItem postItem = new Gson()
                        .fromJson(data.getStringExtra(ThreadActivity.RESULT_POST_CHANGE), PostItem.class);
                int pos = data.getIntExtra(ThreadActivity.RESULT_POST_POS, -1);
                if (linksView.getItems().contains(postItem) && pos >= 0) {
                    // TODO: 2016-04-18 override hashcode to check whether item has actually changed before calling update
                    // TODO: 2016-04-18 Also, use the same mechanism for single item operations in LinkContainerView
                    // TODO: 2016-04-18 handle 400 errors - some posts cannot be modified (archived)
                    if (postItem.isHidden()) {
                        linksView.removeItem(pos);
                    } else {
                        linksView.updateItem(pos, postItem);
                    }
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected void setupComponent() {
        AppComponent appComponent = ((BaseActivity)getActivity()).component();
        MainComponent mainComponent = (MainComponent)appComponent;
        LinksModule linksModule = new LinksModule(this, new LinksFeedDelegate(this));

        component = DaggerSearchComponent.builder()
                .mainComponent(mainComponent)
                .searchModule(new SearchModule(this))
                .linksModule(linksModule)
                .build();
        component.inject(this);

        linksComponent = component.getLinksComponent(linksModule);

        setupLinksFeedLayout();
    }

    private void setupLinksFeedLayout() {
        LinksView linksView = linksComponent.getLinksView();

        if (linksView != null && linksView instanceof LinksFeedDelegate) {
            ((LinksFeedDelegate) linksView).setLinksPresenter(linksComponent.getLinksPresenter());
            linksFeedLayout.setLinksDelegate(linksView);
        }
    }

    @Override
    protected void setupToolbar() {
        ActionBar supportActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowCustomEnabled(true);
            supportActionBar.setCustomView(R.layout.fragment_search_toolbar);

            View customView = supportActionBar.getCustomView();
            if (customView instanceof RelativeLayout) {
                customToolbar = (RelativeLayout) customView;

                setupToolbarSearch();
                setupToolbarSearchClear();
                setupToolbarSearchFilter();
            }
        }
    }

    private void setupToolbarSearchFilter() {
        if(customToolbar == null) return;

        searchFilter = customToolbar.findViewById(R.id.search_filter);

        setupFilterContentLayout();

        searchFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove layout from its parent, to set it to another, new parent
                if (filterContentLayout.getParent() != null) {
                    ((ViewGroup) filterContentLayout.getParent()).removeView(filterContentLayout);
                }

                try {
                    dialogUtil.build()
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

    }

    private void setupToolbarSearchClear() {
        if (customToolbar == null) return;

        searchClear = customToolbar.findViewById(R.id.search_clear);

        searchClear.setOnClickListener(v -> {
            searchView.setText("");
            searchView.setHint(getResources().getString(R.string.enter_search_term));
            searchView.setCursorVisible(true);
            toggleKeyboard(true);
        });
    }

    private void setupToolbarSearch() {
        if (customToolbar == null) return;

        searchView = customToolbar.findViewById(R.id.search_view);

        searchView.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            } else if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    event == null ||
                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                //clear params
                params.clear();
                String query = v.getText().toString();
                if (query.length() > 0 && !searchSwipeContainer.isRefreshing()) {
                    params.put("q", query);
                    subreddit = "";
                    linksComponent.getLinksView().search(subreddit, params);
                    searchView.setCursorVisible(false);
                }
            }

            //hide keyboard
            toggleKeyboard(false);

            return true;
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

        //focus on edit text and show keyboard
        searchView.setFocusable(true);
        searchView.requestFocus();
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
            searchSwipeContainer.setProgressViewOffset(false, 0 , 50);
        }
    }

    private void setupFilterContentLayout() {
        filterContentLayout = (RelativeLayout)mInflater.inflate(R.layout.fragment_search_toolbar_filter, null);
        fromSpinner = (Spinner) filterContentLayout.findViewById(R.id.spinner_from);
        sortSpinner = (Spinner) filterContentLayout.findViewById(R.id.spinner_sort);

        ArrayAdapter<String> fromAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, fromArray);
        fromAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<String>(
                getActivity(), R.layout.support_simple_spinner_dropdown_item, sortArray);
        sortAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        fromSpinner.setAdapter(fromAdapter);
        sortSpinner.setAdapter(sortAdapter);

        limitToView = (EditText) filterContentLayout.findViewById(R.id.limit_view);
    }

    private void performPositiveEvent(MaterialDialog dialog) {
        RelativeLayout layout = (RelativeLayout) dialog.getCustomView();
        EditText editText = (EditText) layout.findViewById(R.id.filter_query);

        String query = editText.getText().toString();
        String limitTo = limitToView.getText().toString();

        if(!searchSwipeContainer.isRefreshing()) {
            params.clear();
            params.put("q", (query.trim()));
            params.put("t", fromSpinner.getSelectedItem().toString());
            params.put("sort", sortSpinner.getSelectedItem().toString());

            subreddit = limitTo.trim();

            linksComponent.getLinksView().search(subreddit, params);

            if (!searchView.getText().toString().equals(query)) {
                searchView.setText(query);
            }
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
        linksComponent.getLinksPresenter().registerForEvents();
    }

    @Override
    public void onPause() {
        super.onPause();

        //hide keyboard
        toggleKeyboard(false);
    }


    @Override
    public void onDestroyView() {
        searchPresenter.unregisterForEvents();
        linksComponent.getLinksPresenter().unregisterForEvents();

        ButterKnife.reset(this);
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
//        searchRecyclerView.setVisibility(View.GONE);
        searchSwipeContainer.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        searchSwipeContainer.setRefreshing(false);
//        searchRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }

    @Override
    public void showPanel() {
        ((SlidingUpPanelActivity) getActivity()).showPanel();
    }

    @Override
    public void hidePanel() {
        ((SlidingUpPanelActivity) getActivity()).hidePanel();
    }

    @Override
    public void togglePanel() {
        ((SlidingUpPanelActivity) getActivity()).togglePanel();
    }

    @Override
    public void setPanelHeight(int height) {
        ((SlidingUpPanelActivity) getActivity()).setPanelHeight(height);
    }

    @Override
    public void setPanelView(Fragments fragmentEnum, Bundle bundle) {
//        ((MainActivity)getActivity()).setPanelView(fragmentName);
    }

    @Override
    public void setDraggable(View view) {
        // no implementation
    }

    @Override
    public SlidingUpPanelLayout.PanelState getPanelState() {
        return ((MainActivity)getActivity()).getPanelState();
    }

    @Override
    public void showToolbar() {

    }

    @Override
    public void hideToolbar() {

    }
}
