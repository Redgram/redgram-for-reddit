package com.matie.redgram.data.managers.presenters;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.data.network.api.utils.subscriber.NullCheckSubscriber;
import com.matie.redgram.data.network.api.utils.subscriber.NullSubscriptionExecutor;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseActivity;
import com.matie.redgram.ui.thread.views.ThreadView;

import java.util.List;
import java.util.Map;

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

    private final App app;
    private final ThreadView threadView;
    private final RedditClientInterface redditClient;

    private CompositeSubscription subscriptions;
    private Subscription threadSubscription;

    @Inject
    public ThreadPresenterImpl(ThreadView threadView, App app) {
        this.app = app;
        this.threadView = threadView;
        this.redditClient = app.getRedditClient();
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(!subscriptions.hasSubscriptions()){
            if(threadSubscription != null){
                subscriptions.add(threadSubscription);
            }
        }
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions != null && subscriptions.hasSubscriptions()){
            subscriptions.unsubscribe();
        }
    }

    private Prefs getUserPrefs(){
        Session session = threadView.getParentView().getBaseActivity().getSession();
        if(session != null && session.getUser() != null){
            return session.getUser().getPrefs();
        }
        return null;
    }

    @Override
    public void getThread(String id, Map<String, String> params) {
        params.put("limit", getUserPrefs().getNumComments()+"");
        //todo display the sort in action bar
        params.put("sort", getUserPrefs().getDefaultCommentSort());
        threadView.showLoading();
        threadSubscription = redditClient.getCommentsByArticle(id, params)
                .compose(((BaseActivity)threadView.getParentView()).bindToLifecycle())
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
                        //todo: update post too?
                        List<CommentBaseItem> commentItems = wrapper.getCommentItems();
                        threadView.passDataToCommentsView(commentItems);
                    }
                });
    }

    @Override
    public void vote(PostItem item, int dir) {
        Subscription voteSubscription = redditClient.voteFor(item.getName(), dir)
                .compose(((BaseActivity)threadView.getParentView()).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
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
                    public void executeOnError(Throwable e) {
                        threadView.showErrorMessage(e.toString());
                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(voteSubscription);
        }
    }

    @Override
    public void save(PostItem item, boolean save) {
        Subscription saveSubscription = redditClient.save(item.getName(), save)
                .compose(((BaseActivity)threadView.getParentView()).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        item.setSaved(save);
                        threadView.toggleSave(save);
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        threadView.showErrorMessage(e.toString());
                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(saveSubscription);
        }
    }

    @Override
    public void hide(PostItem item, boolean hide) {
        Subscription hideSubscription = redditClient.hide(item.getName(), hide)
                .compose(((BaseActivity)threadView.getParentView()).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        item.setHidden(hide);
                        if(hide){
                            threadView.toggleUnHide();
                        }
                    }

                    @Override
                    public void executeOnError(Throwable e) {

                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(hideSubscription);
        }
    }
}
