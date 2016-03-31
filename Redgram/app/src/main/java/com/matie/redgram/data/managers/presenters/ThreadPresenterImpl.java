package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.network.api.reddit.RedditClient;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.thread.views.ThreadView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static rx.android.app.AppObservable.bindActivity;

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
        threadView.showLoading();
        threadSubscription = (Subscription)bindActivity(threadView.getBaseActivity(), redditClient.getCommentsByArticle(id, null))
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
//                        PostPreviewFragment postFragment = threadView.getAdapter().getPostPreviewFragment();
                        List<CommentBaseItem> commentItems = wrapper.getCommentItems();
                        threadView.passDataToCommentsView(commentItems);
                    }
                });
    }


}
