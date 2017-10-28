package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.R;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.home.HomeViewWrapper;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.common.base.BaseFragment;
import com.matie.redgram.ui.home.views.HomeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by matie on 12/04/15.
 */

public class HomePresenterImpl implements HomePresenter{
    private final App app;
    private final HomeView homeView;
    private final RedditClientInterface redditClient;
    private final DatabaseManager databaseManager;
    private final Session session;
    private CompositeSubscription subscriptions;
    private List<SubredditItem> subredditItems;

    //global subscriptions
    private Subscription homeWrapperSubscription;


    /**
     * Called onCreate(View) of MainActivity/Fragment
     * @param homeView
     */
    @Inject
    public HomePresenterImpl(HomeView homeView, App app) {
        this.app = app;
        this.homeView = homeView;
        this.redditClient = app.getRedditClient();
        this.databaseManager = app.getDatabaseManager();
        this.subredditItems = new ArrayList<>();

        this.session = homeView.getParentView().getBaseActivity().getSession();
    }

    /**
     * Called onResume of MainActivity/Fragment
     */
    @Override
    public void registerForEvents() {
        if(subscriptions == null)
            subscriptions = new CompositeSubscription();

        if(!subscriptions.isUnsubscribed()){
            if(homeWrapperSubscription != null){
                subscriptions.add(homeWrapperSubscription);
            }
        }
    }
    /**
     * Called onPause of MainActivity/Fragment
     */
    @Override
    public void unregisterForEvents() {
        if(subscriptions != null && subscriptions.hasSubscriptions()){
            subscriptions.unsubscribe();
        }
    }

    /**
     * gets Subscriptions for now
     */
    @Override
    public void getHomeViewWrapper() {
        homeView.showLoading();

        Map<String, String> params = new HashMap<>();
        if(getPrefs() != null){
            params.put("limit", getPrefs().getNumSites()+"");
        }

        Observable<Listing<PostItem>> linksObservable =
                redditClient.getListing(app.getResources().getString(R.string.default_filter).toLowerCase(),
                        params, null);

        Map<String,String> subparams = new HashMap<String, String>();
        subparams.put("limit", "100");

        Listing<SubredditItem> storedListing =  getSubredditsFromCache();
        Observable<Listing<SubredditItem>> subredditsObservable;
        if(storedListing != null){
            subredditsObservable = Observable.just(storedListing);
        }else{
            if(session.getUser() != null && User.USER_GUEST.equalsIgnoreCase(session.getUser().getUserType())){
                subredditsObservable = redditClient.getSubreddits("default", subparams);
            }else{
                //auth user
                subredditsObservable = redditClient.getSubscriptions(subparams);
            }
        }

        homeWrapperSubscription = Observable
                .zip(linksObservable, subredditsObservable, Observable.just((storedListing != null)), (links, subredditListing, inStore) -> {
                    HomeViewWrapper homeViewWrapper = new HomeViewWrapper();
                    homeViewWrapper.setSubreddits(subredditListing);
                    homeViewWrapper.setIsSubredditsCached(inStore);
                    homeViewWrapper.setLinks(links);
                    return homeViewWrapper;
                })
                .compose(((BaseFragment)homeView.getParentView()).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeViewWrapper>() {
                    @Override
                    public void onCompleted() {
                        homeView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //homeView.hideLoading();
                        homeView.showErrorMessage(e.toString());
                    }

                    @Override
                    public void onNext(HomeViewWrapper homeViewWrapper) {
                        //dealing with links
                        homeView.loadLinksContainer(homeViewWrapper.getLinks());

                        //dealing with the subreddits
                        Listing<SubredditItem> subredditListing = homeViewWrapper.getSubreddits();
                        subredditItems.addAll(subredditListing.getItems());
                        Collections.sort(subredditItems, new Comparator<SubredditItem>() {
                            @Override
                            public int compare(SubredditItem lhs, SubredditItem rhs) {
                                return lhs.getName().compareToIgnoreCase(rhs.getName());
                            }
                        });
                        //add to db
                        if(!homeViewWrapper.getIsSubredditsCached()){
                            setSubredditsInCache(homeViewWrapper.getSubreddits());
                        }
                    }
                });
    }

    @Override
    public List<String> getSubreddits() {
        List<String> subredditNames = new ArrayList<>();

        if(subredditItems != null && subredditItems.size() > 0){
            for(SubredditItem item : subredditItems){
                subredditNames.add(item.getName());
            }
        }else{
            Listing<SubredditItem> storedListing = getSubredditsFromCache();
            if(storedListing != null){
                for(SubredditItem item : storedListing.getItems()){
                    subredditNames.add(item.getName());
                }
            }
        }

        return subredditNames;
    }

    public Listing<SubredditItem> getSubredditsFromCache() {
        List<Subreddit> subreddits = databaseManager.getSubreddits();
        if(!subreddits.isEmpty()){
            return buildSubredditListing(subreddits);
        }
        return null;
    }

    private void setSubredditsInCache(Listing<SubredditItem> listing) {
        databaseManager.setSubreddits(listing.getItems());
    }

    private Listing<SubredditItem> buildSubredditListing(List<Subreddit> subreddits) {
        Listing<SubredditItem> listing = new Listing<>();
        List<SubredditItem> items = new ArrayList<>();
        for(Subreddit subreddit : subreddits){
            SubredditItem item = new SubredditItem();
            item.setName(subreddit.getName());
            items.add(item);
        }
        listing.setItems(items);
        return listing;
    }

    private Prefs getPrefs(){
        Session session = homeView.getParentView().getBaseActivity().getSession();
        if(session != null && session.getUser() != null){
            return session.getUser().getPrefs();
        }
        return null;
    }
}
