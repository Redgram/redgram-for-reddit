package com.matie.redgram.data.managers.presenters;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.matie.redgram.data.managers.preferences.PreferenceManager;
import com.matie.redgram.data.models.main.home.HomeViewWrapper;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.previews.PostPreviewFragment;
import com.matie.redgram.ui.common.views.widgets.postlist.PostRecyclerView;
import com.matie.redgram.ui.home.views.HomeView;
import com.matie.redgram.ui.thread.views.ThreadView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;
import static rx.android.app.AppObservable.bindFragment;

/**
 * Created by matie on 2016-02-10.
 */
public class ThreadPresenterImpl implements ThreadPresenter {

    final private ThreadView threadView;
    final private RedditClient redditClient;

    private CompositeSubscription subscriptions;
    private Subscription threadSubscription;

    @Inject
    public ThreadPresenterImpl(ThreadView threadView, App app) {
        this.threadView = threadView;
        this.redditClient = app.getRedditClient();
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(subscriptions.isUnsubscribed()){
            if(threadSubscription != null){
                subscriptions.add(threadSubscription);
            }
        }
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions.hasSubscriptions() || subscriptions != null){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getThread(String id) {
        threadSubscription = (Subscription)bindActivity(threadView.getActivity(), redditClient.getCommentsByArticle(id, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentsWrapper>() {

                    @Override
                    public void onCompleted(){
                        threadView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e){
                        threadView.hideLoading();
                        threadView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(CommentsWrapper wrapper) {
                        PostPreviewFragment postFragment = threadView.getAdapter().getPostPreviewFragment();
                        if(postFragment != null && wrapper.getPostItem().isSelf()){
                           postFragment.refreshPost(wrapper.getPostItem());
                        }
                        List<CommentBaseItem> commentItems = wrapper.getCommentItems();
                        threadView.getAdapter().getCommentsPreviewFragment().refreshComments(commentItems);
                    }
                });
    }


}
