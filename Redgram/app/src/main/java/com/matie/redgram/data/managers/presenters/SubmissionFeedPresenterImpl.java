package com.matie.redgram.data.managers.presenters;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.data.network.api.utils.subscriber.NullCheckSubscriber;
import com.matie.redgram.data.network.api.utils.subscriber.NullSubscriptionExecutor;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.submission.SubmissionFeedView;
import com.matie.redgram.ui.submission.links.views.LinksView;

import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public abstract class SubmissionFeedPresenterImpl extends BasePresenterImpl
        implements SubmissionFeedPresenter {

    @Inject
    public SubmissionFeedPresenterImpl(SubmissionFeedView submissionFeedView, App app) {
        super(parentView, app);
    }
}
