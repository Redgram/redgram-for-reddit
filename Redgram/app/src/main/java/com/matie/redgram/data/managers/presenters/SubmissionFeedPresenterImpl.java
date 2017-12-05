package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.submission.SubmissionFeedView;

import javax.inject.Inject;


public abstract class SubmissionFeedPresenterImpl extends BasePresenterImpl
        implements SubmissionFeedPresenter {
    @Inject
    public SubmissionFeedPresenterImpl(SubmissionFeedView submissionFeedView, App app) {
        super(submissionFeedView, app);
    }
}
