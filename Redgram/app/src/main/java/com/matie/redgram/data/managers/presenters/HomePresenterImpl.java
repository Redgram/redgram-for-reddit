package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenterImpl;
import com.matie.redgram.data.managers.storage.db.DatabaseManager;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.db.Session;
import com.matie.redgram.data.models.db.Subreddit;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.home.HomeViewWrapper;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.network.api.reddit.RedditClientInterface;
import com.matie.redgram.ui.App;
import com.matie.redgram.ui.home.views.HomeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomePresenterImpl extends BasePresenterImpl implements HomePresenter {

    private final HomeView homeView;
    private final RedditClientInterface redditClient;
    private final DatabaseManager databaseManager;
    private final Session session;
    private List<SubredditItem> subredditItems;

    @Inject
    public HomePresenterImpl(HomeView homeView, App app) {
        super(homeView, app);
        this.homeView = (HomeView) view;
        this.redditClient = app.getRedditClient();
        this.databaseManager = databaseManager();
        this.subredditItems = new ArrayList<>();

        this.session = databaseManager.getSession();
    }

    @Override
    public void getHomeViewWrapper() {
        homeView.showLoading();

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

        Subscription homeWrapperSubscription = Observable
                .zip(subredditsObservable, Observable.just((storedListing != null)), (subredditListing, inStore) -> {
                    HomeViewWrapper homeViewWrapper = new HomeViewWrapper();
                    homeViewWrapper.setSubreddits(subredditListing);
                    homeViewWrapper.setIsSubredditsCached(inStore);
                    return homeViewWrapper;
                })
                .compose(getTransformer())
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
                        //dealing with the subreddits
                        Listing<SubredditItem> subredditListing = homeViewWrapper.getSubreddits();

                        subredditItems.addAll(subredditListing.getItems());

                        Collections.sort(subredditItems,
                                (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));

                        //add to db
                        if(!homeViewWrapper.getIsSubredditsCached()){
                            setSubredditsInCache(homeViewWrapper.getSubreddits());
                        }
                    }
                });

        addSubscription(homeWrapperSubscription);
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

    private Listing<SubredditItem> getSubredditsFromCache() {
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

    private Prefs getPrefs() {
        return databaseManager.getSessionPreferences();
    }
}
