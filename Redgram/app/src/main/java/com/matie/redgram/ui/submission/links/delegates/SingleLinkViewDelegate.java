package com.matie.redgram.ui.submission.links.delegates;


import android.content.Context;

import com.matie.redgram.data.managers.presenters.LinksPresenter;
import com.matie.redgram.ui.submission.SubmissionViewDelegate;
import com.matie.redgram.ui.submission.links.views.SingleLinkView;

public class SingleLinkViewDelegate extends SubmissionViewDelegate implements SingleLinkView {

    private final LinksPresenter linksPresenter;

    public SingleLinkViewDelegate(LinksPresenter linksPresenter) {
        this.linksPresenter = linksPresenter;
    }

    @Override
    public void showHideUndoOption(Context context) {

    }

    @Override
    public void votePost(int position, Integer dir) {

    }

    @Override
    public void savePost(int position, boolean save) {

    }

    @Override
    public void hidePost(int position) {

    }

    @Override
    public void reportPost(Context context, int position) {

    }

    @Override
    public void deletePost(Context context, int position) {

    }

    @Override
    public void loadCommentsForPost(Context context, int position) {

    }

    @Override
    public void sharePost(Context context, int position) {

    }

    @Override
    public void visitSubreddit(Context context, String subredditName) {

    }

    @Override
    public void visitProfile(Context context, String username) {

    }

    @Override
    public void openInBrowser(Context context, int position) {

    }

    @Override
    public void copyItemLink(Context context, int position) {

    }

    @Override
    public void viewWebMedia(Context context, int position) {

    }

    @Override
    public void viewImageMedia(Context context, int position, boolean loaded) {

    }

    @Override
    public void callAgeConfirmDialog(Context context) {

    }
}
