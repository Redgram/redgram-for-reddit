package com.matie.redgram.ui.submission.links.delegates;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.LinksPresenterImpl;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.adapters.PostAdapterBase;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.submission.links.views.LinksView;
import com.matie.redgram.ui.thread.ThreadActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class LinksViewDelegate implements LinksView {

    private final SubmissionFeedPresenter submissionFeedPresenter;
    private final Gson gson = new Gson();

    private PostRecyclerView containerRecyclerView;
    protected ProgressBar containerProgressBar;

    private String subredditChoice = null;
    private String filterChoice = null;
    private Map<String,String> params = new HashMap<>();

    //recycler view listeners to add.
    private RecyclerView.OnScrollListener loadMoreListener;
    private LinearLayoutManager layoutManager;

    @Inject
    public LinksViewDelegate(final SubmissionFeedPresenter submissionFeedPresenter) {
        this.submissionFeedPresenter = submissionFeedPresenter;
    }

    public void fetchLinks(final Context context) {
        if (context == null) return;

        filterChoice = context.getResources().getString(R.string.default_filter).toLowerCase();
        submissionFeedPresenter.getListing(subredditChoice, filterChoice, params);
    }

    private Prefs getPrefs() {
        return submissionFeedPresenter.databaseManager().getSessionPreferences();
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

    public void setContentView(RecyclerView contentView) {
        containerRecyclerView = (PostRecyclerView) contentView;

        setupRecyclerView();
        setupListeners();
    }

    public void setLoadingView(ProgressBar loadingView) {
        containerProgressBar = loadingView;
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
                            submissionFeedPresenter.getMoreListing(subredditChoice, filterChoice , params);
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

//        containerRecyclerView.setListener(this);
    }

    public void setLoadMoreId(String id) {
        ((LinksPresenterImpl) submissionFeedPresenter).setLoadMoreId(id);
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
        submissionFeedPresenter.getListing(subredditChoice, filterChoice, params);
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

        submissionFeedPresenter.getListing(subredditChoice, filterChoice, this.params);
    }

    @Override
    public void search(String subredditChoice, @NonNull Map<String, String> params) {
        if (subredditChoice == null) return;

        this.filterChoice = null;
        this.subredditChoice = subredditChoice;
        this.params = params;

        submissionFeedPresenter.searchListing(subredditChoice, params);
    }

    @Override
    public void showHideUndoOption(final Context context) {
        if (context instanceof CoordinatorLayoutInterface) {
            String msg = context.getResources().getString(R.string.item_hidden);
            String actionMsg = context.getResources().getString(R.string.undo);
            View.OnClickListener onClickListener = v -> submissionFeedPresenter.unHide();

            ((CoordinatorLayoutInterface) context)
                    .showSnackBar(msg, Snackbar.LENGTH_LONG, actionMsg, onClickListener, null);
        }
    }

    @Override
    public void votePost(int position, Integer dir) {
        submissionFeedPresenter.voteFor(position, getItem(position).getName(), dir);
    }

    @Override
    public void savePost(int position, boolean save) {
        submissionFeedPresenter.save(position, getItem(position).getName(), save);
    }

    @Override
    public void hidePost(int position) {
        submissionFeedPresenter.hide(position, getItem(position).getName(), true);
    }

    @Override
    public void reportPost(final Context context, int position) {
        MaterialDialog.SingleButtonCallback callback =
                (materialDialog, dialogAction) -> submissionFeedPresenter.report(position);
        LinksHelper.callReportDialog(context, callback);
    }

    @Override
    public void deletePost(final Context context, int position) {
        submissionFeedPresenter.delete(position);
    }

    @Override
    public void loadCommentsForPost(final Context context, int position) {
        String key = context.getResources().getString(R.string.main_data_key);
        String posKey = context.getResources().getString(R.string.main_data_position);

        Intent intent = new Intent(context, ThreadActivity.class);
        intent.putExtra(key, gson.toJson(getItem(position)));
        intent.putExtra(posKey, position);

        ((BaseActivity) context).openIntentForResult(intent, ThreadActivity.REQ_CODE);
    }

    @Override
    public void sharePost(final Context context, int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getShareCallback(context, item);
        LinksHelper.showExternalDialog(context, "Share", callback);
    }

    @Override
    public void visitSubreddit(final Context context, String subredditName) {
        LinksHelper.openResult(context, subredditName, LinksHelper.SUB);
    }

    @Override
    public void visitProfile(final Context context, String username) {
        LinksHelper.openResult(context, username, LinksHelper.PROFILE);
    }

    @Override
    public void openInBrowser(final Context context, int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getBrowseCallback(context, item);
        LinksHelper.showExternalDialog(context, "Open in Browser",callback);
    }

    @Override
    public void copyItemLink(final Context context, int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getCopyCallback(context, item);
        LinksHelper.showExternalDialog(context, "Copy", callback);
    }

    @Override
    public void viewWebMedia(final Context context, int position) {
        Bundle bundle = new Bundle();
        String key = context.getResources().getString(R.string.main_data_key);
        bundle.putString(key, gson.toJson(getItem(position)));
        ((SlidingUpPanelActivity)context).setPanelView(Fragments.WEB_PREVIEW, bundle);
    }

    @Override
    public void viewImageMedia(final Context context, int position, boolean loaded) {
        if (loaded) {
            PostItem item = getItem(position);

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(ImageRequest
                            .fromUri(Uri.parse(item.getUrl())));

            if (cacheKey != null) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);

                File localFile;
                if (resource != null) {
                    localFile = ((FileBinaryResource) resource).getFile();

                    Bundle bundle = new Bundle();

                    bundle.putString(context.getResources().getString(R.string.local_cache_key), localFile.getPath());

                    bundle.putString(context.getResources().getString(R.string.main_data_key), gson.toJson(item));

                    ((SlidingUpPanelActivity)context).setPanelView(Fragments.IMAGE_PREVIEW, bundle);
                }
            }
        }
    }

    @Override
    public void callAgeConfirmDialog(final Context context) {
        MaterialDialog.SingleButtonCallback callback = (materialDialog, dialogAction) -> {
            final Prefs prefs = getPrefs();
            if (!prefs.isOver18()) {
                submissionFeedPresenter.confirmAge();
            } else if (prefs.isDisableNsfwPreview()) {
                //change preferences
                submissionFeedPresenter.enableNsfwPreview();
            }
        };

        LinksHelper.callAgeConfirmDialog(context, callback);
    }
    //endregion
}
