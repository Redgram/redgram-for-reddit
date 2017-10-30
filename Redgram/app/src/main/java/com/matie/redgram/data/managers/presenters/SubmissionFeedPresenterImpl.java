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
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.common.utils.widgets.ToastHandler;
import com.matie.redgram.ui.common.views.ContentView;
import com.matie.redgram.ui.submissions.SubmissionFeedView;

import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Links Presenter Implementation
 */
public class SubmissionFeedPresenterImpl extends BasePresenterImpl implements SubmissionFeedPresenter {

    final private SubmissionFeedView submissionFeedView;
    final private ContentView parentView;
    final private RedditClientInterface redditClient;
    final private ToastHandler toastHandler;

    private CompositeSubscription subscriptions;
    //global subscriptions
    private Subscription listingSubscription;

    //states
    private String loadMoreId;
    private PostItem removedItem;
    private int removedItemPosition;


    @Inject
    public SubmissionFeedPresenterImpl(SubmissionFeedView submissionFeedView, ContentView parentView, App app) {
        super(parentView, app);
        this.submissionFeedView = submissionFeedView;
        this.parentView = parentView;
        this.redditClient = app.getRedditClient();
        this.toastHandler = app.getToastHandler();
        this.loadMoreId = "";
    }

    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(!subscriptions.isUnsubscribed()) {
            if (listingSubscription != null) {
                subscriptions.add(listingSubscription);
            }
        }
    }

    @Override
    public void unregisterForEvents() {
        if(subscriptions != null && subscriptions.hasSubscriptions()){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void getListing(String subreddit, String front, Map<String, String> params) {
        if(params.containsKey("after")){
            params.remove("after");
        }
        params.put("limit", getPrefs().getNumSites()+"");
        parentView.showLoading();
        listingSubscription = getListingSubscription(subreddit, front, params, true);
    }

    @Override
    public void getMoreListing(String subreddit, @Nullable String front, Map<String, String> params) {
        params.put("after", loadMoreId);
        params.put("limit", getPrefs().getNumSites()+"");
        submissionFeedView.showLoading();
        if(front != null){
            listingSubscription = getListingSubscription(subreddit, front, params, false);
        }else{
            listingSubscription = getSearchSubscription(subreddit, params, false);
        }

    }

    @Override
    public void searchListing(String subreddit, Map<String, String> params) {
        if(params.containsKey("after")){
            params.remove("after");
        }

        parentView.showLoading();
        listingSubscription = getSearchSubscription(subreddit, params, true);

        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(listingSubscription);
        }
    }

    @Override
    public void voteFor(int position, String name, Integer dir) {
        Subscription voteSubscription = redditClient.voteFor(name, dir)
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
                            submissionFeedView.getItem(position).setLikes("true");
                        }else if(dir == -1){
                            submissionFeedView.getItem(position).setLikes("false");

                        }else{
                            submissionFeedView.getItem(position).setLikes(null);
                        }
                        submissionFeedView.updateItem(position, submissionFeedView.getItem(position));
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(voteSubscription);
        }
    }

    @Override
    public void hide(int position, String name, boolean showUndo) {
        Subscription hideSubscription = redditClient.hide(name, true)
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        if(showUndo){
                            removedItemPosition = position;
                            removedItem = submissionFeedView.removeItem(position);
                            submissionFeedView.showHideUndoOption();
                        } //else already removed and updated list
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }
                }));

        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(hideSubscription);
        }
    }

    @Override
    public void unHide() {
        //set local variables because the global values are reset on button click
        int removedItemPos = removedItemPosition;
        PostItem removedPost = removedItem;

        Subscription unHideSubscription = redditClient.hide(removedPost.getName(), true)
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        //null check made
                        submissionFeedView.insertItem(removedItemPos, removedPost);
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(unHideSubscription);
        }
    }

    @Override
    public void save(int position, String name, boolean save) {
        Subscription saveSubscription = redditClient.save(name, save)
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        //null check is already done
                        submissionFeedView.getItem(position).setSaved(save);
                        submissionFeedView.updateItem(position, submissionFeedView.getItem(position));
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }
                }));
        if (!subscriptions.isUnsubscribed()){
            subscriptions.add(saveSubscription);
        }
    }

    // TODO: 2016-03-25 when the user is able to submit
    @Override
    public void delete(int position) {

    }

    @Override
    public void report(int position) {
        final String name = submissionFeedView.getItem(position).getName();
        Subscription reportSubscription = redditClient.report(name)
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCheckSubscriber<>(new NullSubscriptionExecutor<JsonElement>() {
                    @Override
                    public void executeOnCompleted() {

                    }

                    @Override
                    public void executeOnNext(JsonElement data) {
                        submissionFeedView.removeItem(position);
                        hide(position, name, false);
                        toastHandler.showBackgroundToast("Reported", Toast.LENGTH_LONG);
                    }

                    @Override
                    public void executeOnError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }
                }));
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(reportSubscription);
        }
    }

    @Override
    public void confirmAge() {

        AuthPrefs prefs = new AuthPrefs();
        prefs.setOver18(true);

        Subscription subscription = redditClient.updatePrefs(prefs)
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthPrefs>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        submissionFeedView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(AuthPrefs prefs) {
                        app.getDatabaseManager().setPrefs(prefs);
                        //realm Prefs object will recognize a change a refresh the list in the UI
                    }
                });
        if(!subscriptions.isUnsubscribed()){
            subscriptions.add(subscription);
        }
    }

    @Override
    public void enableNsfwPreview() {
        Realm realm = submissionFeedView.getParentView().getBaseActivity().getRealm();
        if(realm != null){
            realm.executeTransaction(instance -> getPrefs().setDisableNsfwPreview(false));
        }
    }

    private Subscription getListingSubscription(@Nullable String subreddit, @Nullable String filter, Map<String,String> params, boolean isNew){
        Observable<Listing<PostItem>> targetObservable;

        if(subreddit != null){
            if(filter != null)
                targetObservable = redditClient.getSubredditListing(subreddit,filter, params, ((!isNew)? submissionFeedView.getItems() : null));
            else
                targetObservable = redditClient.getSubredditListing(subreddit, params, ((!isNew)? submissionFeedView.getItems() : null) );
        }else{
            targetObservable = redditClient.getListing(filter, params, ((!isNew)? submissionFeedView.getItems() : null));
        }

        return buildSubscription(targetObservable, isNew);
    }

    @SuppressWarnings("unchecked")
    // TODO: 2016-04-21 share Subscriber with getSearchSubscription
    private Subscription buildSubscription(Observable<Listing<PostItem>> observable, boolean isNew){
        return observable
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Listing>() {
                    @Override
                    public void onCompleted() {
                        if(isNew){
                            parentView.hideLoading();
                        }else{
                            submissionFeedView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isNew){
                            parentView.hideLoading();
                        }else{
                            submissionFeedView.hideLoading();
                        }
                        submissionFeedView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(Listing wrapper) {
                        if(isNew){
                            submissionFeedView.updateList(wrapper.getItems());
                        }else{
                            submissionFeedView.getItems().addAll(wrapper.getItems());
                            submissionFeedView.updateList();
                        }
                        loadMoreId = wrapper.getAfter();
                    }
                });
    }

    @SuppressWarnings("unchecked")
    private Subscription getSearchSubscription(String subreddit, Map<String, String> params, boolean isNew) {
        return redditClient.executeSearch(subreddit, params, ((!isNew) ? submissionFeedView.getItems() : null))
                .compose(getTransformer())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Listing>() {
                    @Override
                    public void onCompleted() {
                        //hide progress and show list
                        if(isNew){
                            parentView.hideLoading();
                        }else{
                            submissionFeedView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isNew){
                            parentView.hideLoading();
                        }else{
                            submissionFeedView.hideLoading();
                        }
                        parentView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(Listing wrapper) {
                        if(isNew){
                            submissionFeedView.getItems().clear();
                            submissionFeedView.updateList(wrapper.getItems());
                        }else{
                            submissionFeedView.getItems().addAll(wrapper.getItems());
                            submissionFeedView.updateList();
                        }
                        loadMoreId = wrapper.getAfter();
                    }
                });
    }

    public void setRemovedItem(PostItem removedItem) {
        this.removedItem = removedItem;
    }

    public void setRemovedItemPosition(int removedItemPosition) {
        this.removedItemPosition = removedItemPosition;
    }

    public void setLoadMoreId(String loadMoreId) {
        this.loadMoreId = loadMoreId;
    }

    private Prefs getPrefs(){
        return submissionFeedView.getParentView().getBaseActivity().getSession().getUser().getPrefs();
    }
}
