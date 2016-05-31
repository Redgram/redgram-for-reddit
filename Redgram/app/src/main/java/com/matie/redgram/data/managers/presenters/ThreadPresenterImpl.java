package com.matie.redgram.data.managers.presenters;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.main.items.PostItem;
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

/**
 * Thread Presenter Implementation
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
        threadSubscription = redditClient.getCommentsByArticle(id, null)
                .compose(threadView.getBaseActivity().bindToLifecycle())
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

    @Override
    public void vote(PostItem item, int dir) {
        Subscription voteSubscription = redditClient.voteFor(item.getName(), dir)
                .compose(threadView.getBaseActivity().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {
                        if(dir == 1){
                            item.setLikes("true");
                        }else if(dir == -1){
                            item.setLikes("false");
                        }else{
                            item.setLikes(null);
                        }
                        threadView.toggleVote(dir);
                    }

                    @Override
                    public void onError(Throwable e) {
                        threadView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(JsonElement redditObject) {
                    }
                });
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(voteSubscription);
        }
    }

    @Override
    public void save(PostItem item, boolean save) {
        Subscription saveSubscription = redditClient.save(item.getName(), save)
                .compose(threadView.getBaseActivity().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {
                        item.setSaved(save);
                        threadView.toggleSave(save);
                    }

                    @Override
                    public void onError(Throwable e) {
                        threadView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(JsonElement redditObject) {
                    }
                });
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(saveSubscription);
        }
    }

    @Override
    public void hide(PostItem item, boolean hide) {
        Subscription hideSubscription = redditClient.hide(item.getName(), hide)
                .compose(threadView.getBaseActivity().bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {
                        item.setHidden(hide);
                        if(hide){
                            threadView.toggleUnHide();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        threadView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(JsonElement redditObject) {
                    }
                });
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(hideSubscription);
        }
    }
}
