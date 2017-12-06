package com.matie.redgram.ui.submission.links.delegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.views.adapters.PostAdapterBase;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.submission.links.views.LinksView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class LinksFeedDelegate implements LinksView {

    private final LinksPresenter linksPresenter;
    private final Gson gson = new Gson();
    private ProgressBar containerProgressBar;

    // state
    private String subredditChoice = null;
    private String filterChoice = null;
    private Map<String,String> params = new HashMap<>();

    // recycler view
    private PostRecyclerView containerRecyclerView;
    private RecyclerView.OnScrollListener loadMoreListener;
    private LinearLayoutManager layoutManager;

    @Inject
    public LinksFeedDelegate(final LinksPresenter linksPresenter) {
        this.linksPresenter = linksPresenter;
    }

    @Override
    public void fetchFeed() {
        if (containerRecyclerView == null) return;

        final Context context = containerRecyclerView.getContext();
        filterChoice = context.getResources().getString(R.string.default_filter).toLowerCase();
        linksPresenter.getListing(subredditChoice, filterChoice, params);
    }

    private Prefs getPrefs() {
        return linksPresenter.databaseManager().getSessionPreferences();
    }

    //region setters

    @Override
    public void showLoading() {
        containerProgressBar.setVisibility(View.VISIBLE);
        containerRecyclerView.removeOnScrollListener(loadMoreListener);
    }

    @Override
    public void hideLoading() {
        containerRecyclerView.addOnScrollListener(loadMoreListener);
        containerProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }

    public void setContentView(View contentView) {
        containerRecyclerView = (PostRecyclerView) contentView;

        setupRecyclerView();
        setupListeners();
    }

    public void setLoadingView(View loadingView) {
        containerProgressBar = (ProgressBar) loadingView;
    }

    private void setupListeners() {
        // this listener is responsible for LOAD MORE. This listener object will be added on startup. Removed when
        // a network call is done and added again when the requested network call is either COMPLETED or had an ERROR.
        loadMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(containerRecyclerView != null && containerRecyclerView.getChildCount() > 0) {
                        int lastItemPosition = getItems().size() - 1;
                        if(layoutManager.findLastCompletelyVisibleItemPosition() == lastItemPosition) {
                            linksPresenter.getMoreListing(subredditChoice, filterChoice , params);
                        }
                    }
                }
            }
        };
    }

    private void setupRecyclerView() {
        containerRecyclerView.getItemAnimator().setChangeDuration(0);
        layoutManager = (LinearLayoutManager) containerRecyclerView.getLayoutManager();

        containerRecyclerView.addOnScrollListener(loadMoreListener);
        containerRecyclerView.setListener(this);
    }

    public void setLoadMoreId(String id) {
        ((LinksPresenterImpl) linksPresenter).setLoadMoreId(id);
    }
    //endregion

    //region LinksView interface
    @Override
    public PostItem getItem(int position) {
        return ((PostAdapterBase) containerRecyclerView.getAdapter()).getItem(position);
    }

    @Override
    public List<PostItem> getItems() {
        return ((PostAdapterBase) containerRecyclerView.getAdapter()).getItems();
    }

    @Override
    public void updateList() {
        containerRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateRestOfList(int position) {
        RecyclerView.Adapter adapter = containerRecyclerView.getAdapter();
        adapter.notifyItemRangeChanged(position, adapter.getItemCount() - position);
    }

    @Override
    public void updateItem(int position, PostItem postItem) {
        getItems().set(position, postItem);
        containerRecyclerView.getAdapter().notifyItemChanged(position);
    }

    @Override
    public PostItem removeItem(int position) {
        PostItem removedItem = getItems().remove(position);
        containerRecyclerView.getAdapter().notifyItemRemoved(position);
        updateRestOfList(position);
        return removedItem;
    }

    @Override
    public void insertItem(int position, PostItem removedPost) {
        getItems().add(position, removedPost);
        containerRecyclerView.getAdapter().notifyItemInserted(position);
        updateRestOfList(position);
    }

    @Override
    public void updateList(List<PostItem> items) {
        containerRecyclerView.replaceWith(items);
    }

    @Override
    public void refreshView() {
        linksPresenter.getListing(subredditChoice, filterChoice, params);
    }

    @Override
    public void refreshView(@Nullable String subredditChoice, @Nullable String filterChoice, @NonNull Map<String, String> params) {
       if (subredditChoice == null || filterChoice == null) return;

        //could be null
        this.subredditChoice = subredditChoice;
        this.filterChoice = filterChoice;
        this.params = params;

        refreshView();
    }

    @Override
    public void sortView(@NonNull String filterChoice, @Nullable Map<String, String> params) {
        if(params == null) {
            this.params.clear();
        } else {
            this.params = params;
        }

        this.filterChoice = filterChoice;

        linksPresenter.getListing(subredditChoice, filterChoice, this.params);
    }

    @Override
    public void search(String subredditChoice, @NonNull Map<String, String> params) {
        if (subredditChoice == null) return;

        this.filterChoice = null;
        this.subredditChoice = subredditChoice;
        this.params = params;

        linksPresenter.searchListing(subredditChoice, params);
    }

    @Override
    public void showHideUndoOption(final Context context) {
        if (context instanceof CoordinatorLayoutInterface) {
            String msg = context.getResources().getString(R.string.item_hidden);
            String actionMsg = context.getResources().getString(R.string.undo);
            View.OnClickListener onClickListener = v -> linksPresenter.unHide();

            ((CoordinatorLayoutInterface) context)
                    .showSnackBar(msg, Snackbar.LENGTH_LONG, actionMsg, onClickListener, null);
        }
    }

    @Override
    public Context getViewContext() {
        return containerRecyclerView.getContext();
    }
    //endregion
}
