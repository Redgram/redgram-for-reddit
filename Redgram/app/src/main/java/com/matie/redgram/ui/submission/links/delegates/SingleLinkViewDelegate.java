package com.matie.redgram.ui.submission.links.delegates;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.gson.Gson;
import com.matie.redgram.R;
import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.common.base.Fragments;
import com.matie.redgram.ui.common.base.SlidingUpPanelActivity;
import com.matie.redgram.ui.common.utils.display.CoordinatorLayoutInterface;
import com.matie.redgram.ui.common.utils.widgets.LinksHelper;
import com.matie.redgram.ui.common.views.widgets.postlist.PostItemView;
import com.matie.redgram.ui.submission.SubmissionViewDelegate;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;
import com.matie.redgram.ui.thread.ThreadActivity;

import java.io.File;

public class SingleLinkViewDelegate extends SubmissionViewDelegate implements SingleLinkView {

    private final LinksPresenter linksPresenter;
    private PostItemView itemView;
    private PostItem item;
    private final Gson gson = new Gson();

    public SingleLinkViewDelegate(LinksPresenter linksPresenter) {
        this.linksPresenter = linksPresenter;
    }

    public void setItemView(PostItemView itemView) {
        this.itemView = itemView;

        if (itemView != null) {
            this.item = itemView.getItem();
        }
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
    public void votePost(int position, Integer dir) {
        linksPresenter.voteFor(position, item.getName(), dir);
    }

    @Override
    public void savePost(int position, boolean save) {
        linksPresenter.save(position, item.getName(), save);
    }

    @Override
    public void hidePost(int position) {
        linksPresenter.hide(position, item.getName(), true);
    }

    @Override
    public void reportPost(final Context context, int position) {
        MaterialDialog.SingleButtonCallback callback =
                (materialDialog, dialogAction) -> linksPresenter.report(position);
        LinksHelper.callReportDialog(context, callback);
    }

    @Override
    public void deletePost(final Context context, int position) {
        linksPresenter.delete(position);
    }

    @Override
    public void loadCommentsForPost(final Context context, int position) {
        String key = context.getResources().getString(R.string.main_data_key);
        String posKey = context.getResources().getString(R.string.main_data_position);

        Intent intent = new Intent(context, ThreadActivity.class);
        intent.putExtra(key, gson.toJson(itemView));
        intent.putExtra(posKey, position);

        ((BaseActivity) context).openIntentForResult(intent, ThreadActivity.REQ_CODE);
    }

    @Override
    public void sharePost(final Context context, int position) {
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
        MaterialDialog.ListCallback callback = LinksHelper.getBrowseCallback(context, item);
        LinksHelper.showExternalDialog(context, "Open in Browser",callback);
    }

    @Override
    public void copyItemLink(final Context context, int position) {
        MaterialDialog.ListCallback callback = LinksHelper.getCopyCallback(context, item);
        LinksHelper.showExternalDialog(context, "Copy", callback);
    }

    @Override
    public void viewWebMedia(final Context context, int position) {
        Bundle bundle = new Bundle();
        String key = context.getResources().getString(R.string.main_data_key);
        bundle.putString(key, gson.toJson(itemView));
        ((SlidingUpPanelActivity)context).setPanelView(Fragments.WEB_PREVIEW, bundle);
    }

    @Override
    public void viewImageMedia(final Context context, int position, boolean loaded) {
        if (loaded) {
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
            final Prefs prefs = linksPresenter.databaseManager().getSessionPreferences();
            if (!prefs.isOver18()) {
                linksPresenter.confirmAge();
            } else if (prefs.isDisableNsfwPreview()) {
                //change preferences
                linksPresenter.enableNsfwPreview();
            }
        };

        LinksHelper.callAgeConfirmDialog(context, callback);
    }
}
