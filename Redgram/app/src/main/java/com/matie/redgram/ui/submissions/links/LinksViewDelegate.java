package com.matie.redgram.ui.submissions.links;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.SubmissionFeedPresenter;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.DialogUtil;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.adapters.PostAdapterBase;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.submissions.views.LinksView;
import com.matie.redgram.ui.thread.ThreadActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class LinksViewDelegate implements LinksView {

    @Inject
    SubmissionFeedPresenter submissionFeedPresenter;

    @Inject
    DialogUtil dialogUtil;

    private PostRecyclerView containerRecyclerView;
    private ProgressBar containerProgressBar;
    private String subredditChoice = null;
    private String filterChoice = null;
    private Map<String,String> params = new HashMap<>();
    private final Gson gson = new Gson();

    public void setContainerRecyclerView(PostRecyclerView containerRecyclerView) {
        this.containerRecyclerView = containerRecyclerView;
    }

    public void setContainerProgressBar(ProgressBar containerProgressBar) {
        this.containerProgressBar = containerProgressBar;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showInfoMessage() {

    }

    @Override
    public void showErrorMessage(String error) {

    }

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
    public void showHideUndoOption() {

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
    public void reportPost(int position) {
        MaterialDialog.SingleButtonCallback callback =
                (materialDialog, dialogAction) -> submissionFeedPresenter.report(position);
        LinksHelper.callReportDialog(dialogUtil, callback);
    }

    @Override
    public void deletePost(int position) {
        submissionFeedPresenter.delete(position);
    }

    @Override
    public void loadCommentsForPost(final Context context, int position) {
        Intent intent = new Intent(context, ThreadActivity.class);
        String key = getResources().getString(R.string.main_data_key);
        String posKey = getResources().getString(R.string.main_data_position);
        intent.putExtra(key, gson.toJson(getItem(position)));
        intent.putExtra(posKey, position);

        Fragment hostingFragment =
                ((BaseActivity) context).getSupportFragmentManager().findFragmentByTag(getHostingFragmentTag());
        if(hostingFragment != null){
            ((BaseFragment)hostingFragment)
                    .openIntentForResult(intent, ThreadActivity.REQ_CODE);
        }else{
            ((BaseActivity) context)
                    .openIntentForResult(intent, ThreadActivity.REQ_CODE);
        }
    }

    @Override
    public void sharePost(final Context context, int position) {
        PostItem item = getItem(position);
        MaterialDialog.ListCallback callback = LinksHelper.getShareCallback(context, item);
        LinksHelper.showExternalDialog(dialogUtil, "Share" ,callback);
    }

    @Override
    public void visitSubreddit(String subredditName) {

    }

    @Override
    public void visitProfile(String username) {

    }

    @Override
    public void openInBrowser(int position) {

    }

    @Override
    public void copyItemLink(int position) {

    }

    @Override
    public void viewWebMedia(int position) {

    }

    @Override
    public void viewImageMedia(int position, boolean loaded) {

    }

    @Override
    public void callAgeConfirmDialog() {

    }
}
