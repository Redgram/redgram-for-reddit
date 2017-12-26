package com.matie.redgram.data.managers.presenters;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentsWrapper;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.data.network.api.utils.subscriber.NullCheckSubscriber;
import com.matie.redgram.data.network.api.utils.subscriber.NullSubscriptionExecutor;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.thread.views.ThreadView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ThreadPresenterImpl extends BasePresenterImpl implements ThreadPresenter {

    private final ThreadView threadView;
    private final RedditClientInterface redditClient;

    @Inject
    public ThreadPresenterImpl(ThreadView threadView, App app) {
        super(threadView, app);
        this.threadView = (ThreadView) view;
        this.redditClient = app.getRedditClient();
    }

    @Override
    public void getThread(String id, Map<String, String> params) {
        Prefs prefs = getUserPrefs();

        if (prefs == null) return;

        params.put("limit", prefs.getNumComments() + "");

        //todo display the sort in action bar
        params.put("sort", prefs.getDefaultCommentSort());

        threadView.showLoading();

        Subscription threadSubscription = redditClient.getCommentsByArticle(id, params)
                .compose(getTransformer())
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
                        List<SubmissionItem> commentItems = wrapper.getCommentItems();
                        threadView.passDataToCommentsView(commentItems);
                    }
                });

        addSubscription(threadSubscription);
    }

    @Override
    public void vote(PostItem item, int dir) {
        Subscription voteSubscription = redditClient.voteFor(item.getName(), dir)
                .compose(getTransformer())
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

        addSubscription(voteSubscription);
    }

    @Override
    public void save(PostItem item, boolean save) {
        Subscription saveSubscription = redditClient.save(item.getName(), save)
                .compose(getTransformer())
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

        addSubscription(saveSubscription);
    }

    @Override
    public void hide(PostItem item, boolean hide) {
        Subscription hideSubscription = redditClient.hide(item.getName(), hide)
                .compose(getTransformer())
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

        addSubscription(hideSubscription);
    }

    @Nullable
    private Prefs getUserPrefs() {
        return databaseManager().getSessionPreferences();
    }

}
