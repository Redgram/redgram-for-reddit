package com.matie.redgram.data.network.api.reddit;

import android.support.annotation.Nullable;

import com.matie.redgram.data.models.api.reddit.RedditSubreddit;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.api.reddit.RedditLink;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.base.RedditProviderBase;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.ui.App;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * Created by matie on 17/04/15.
 */
public class RedditClient extends RedditServiceBase {
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String MODHASH = "modhash";

    private final RedditProviderBase provider;
    private final App app;


    @Inject
    public RedditClient(App app) {
        super(app);

        this.app = app;
        this.provider = getRestAdapter().create(RedditProviderBase.class);
    }

    public Observable<RedditListing> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> subObservable
                = provider.getSubreddit(query, params);

        return getDefaultPostListObservable(subObservable, postItems);

    }

    public Observable<RedditListing> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> subObservable
                = provider.getSubreddit(query, filter, params);

        return getDefaultPostListObservable(subObservable, postItems);
    }


    public Observable<RedditListing> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> searchObservable = null;

        if(!subreddit.isEmpty() && subreddit != null){
            params.put("restrict_sr", "true");
            searchObservable = provider.executeSearch(subreddit, params);
        }else{
            //at least have a "q" param set
            searchObservable = provider.executeSearch(params);
        }

        return getDefaultPostListObservable(searchObservable, postItems);
    }

    public Observable<RedditListing> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems){
        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> listingObservable = provider.getListing(front, params);
        return getDefaultPostListObservable(listingObservable, postItems);
    }

    private Observable<RedditListing> getDefaultPostListObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> listingObservable, List<PostItem> postItems) {

        Observable<List<PostItem>> itemsObservable = getItemsObservable(listingObservable, postItems);
        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(listingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, new Func2<List<PostItem>, Map<String, String>, RedditListing>() {
            @Override
            public RedditListing call(List<PostItem> postItems, Map<String, String> map) {
                RedditListing postItemWrapper = new RedditListing();
                postItemWrapper.setBefore(map.get(BEFORE));
                postItemWrapper.setAfter(map.get(AFTER));
                postItemWrapper.setModHash(map.get(MODHASH));
                postItemWrapper.setItems(postItems);
                return postItemWrapper;
            }
        });
    }

    private Observable<List<PostItem>> getItemsObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> observable, @Nullable List<PostItem> postItems){

        return observable.flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditLink.class)
                .map(link -> mapLinkToPostItem(link))
                .filter(item -> (postItems != null ? filterExistingItems(postItems, item) : true))
                .concatMap(postItem -> {

                    //todo: convert to MP4 and set new link
                    Observable<PostItem> mp4Observable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.GIF
                                    || item.getType() == PostItem.Type.YOUTUBE
                                    || item.getType() == PostItem.Type.GFYCAT);
                    //leave out to render
                    Observable<PostItem> imageObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.IMAGE);
                    //todo: render new view for text only
                    Observable<PostItem> selfObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.SELF);
                    //todo: display new fragment with gridview
                    Observable<PostItem> galleryObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.IMGUR_GALLERY
                                    || item.getType() == PostItem.Type.IMGUR_ALBUM
                                    || item.getType() == PostItem.Type.IMGUR_CUSTOM_GALLERY);
                    //todo: display thumbnail with link to view full source along with ant self text
                    Observable<PostItem> defaultObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.DEFAULT
                                    || item.getType() == PostItem.Type.IMGUR);

                    return Observable.merge(imageObservable, mp4Observable, galleryObservable,
                            selfObservable, defaultObservable);
                })
                .toList();
    }

    /**
     * This function replaces the old item in the list with the new one only if both items have the
     * same id. If replaced, filter out the emitted item. If not, return true to include it.
     *
     * Note: equals method of PostItem is overriden to only return true if ID are the same.
     * @param postItems
     * @param item
     * @return true if the list does not contain the emitted item
     */
    private Boolean filterExistingItems(List<PostItem> postItems, PostItem item) {
        //if the postItems are passed, this means we are loading more objects
        if(postItems.contains(item)){
            int i = postItems.indexOf(item);
            postItems.remove(i);
            postItems.add(i, item);
            return false;
        }
        return true;
    }

    public Observable<RedditListing> getSubreddits(String filter, @Nullable Map<String, String> params){
        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> subredditsListingObservable
                = provider.getSubredditsListing(filter, params);

        Observable<List<SubredditItem>> itemsObservable = subredditsListingObservable
                .flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditSubreddit.class)
                .map(item -> mapToSubredditItem(item))
                .toList();

        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(subredditsListingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, new Func2<List<SubredditItem>, Map<String, String>, RedditListing>() {
            @Override
            public RedditListing call(List<SubredditItem> items, Map<String, String> map) {
                RedditListing postItemWrapper = new RedditListing();
                postItemWrapper.setBefore(map.get(BEFORE));
                postItemWrapper.setAfter(map.get(AFTER));
                postItemWrapper.setModHash(map.get(MODHASH));
                postItemWrapper.setItems(items);
                return postItemWrapper;
            }
        });
    }

    /**
     * Maps attributes that belong to listings only. BEFORE AFTER AND MODHASH.
     * @param responseObservable
     * @return
     */
    private Observable<Map<String,String>> getFieldsObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing>> responseObservable) {
        return responseObservable.map(listing -> mapFieldsToHashMap(listing));
    }

    private Map<String, String> mapFieldsToHashMap(RedditResponse<com.matie.redgram.data.models.api.reddit.RedditListing> listing) {
        Map<String, String> map = new HashMap<String,String>();
        com.matie.redgram.data.models.api.reddit.RedditListing listingData = (com.matie.redgram.data.models.api.reddit.RedditListing)listing.getData();

        map.put(AFTER, listingData.getAfter());
        map.put(BEFORE, listingData.getBefore());
        map.put(MODHASH, listingData.getModhash());

        return map;
    }

    // TODO: 22/09/15 move to a builder class
    private PostItem mapLinkToPostItem(RedditLink link){

        PostItem item = new PostItem();

        item.setId(link.getId());
        item.setName(link.getName());
        item.setScore(link.getScore());
        item.setAuthor(link.getAuthor());
        item.setTime(link.getCreatedUtc().getHourOfDay());
        item.setUrl(link.getUrl());
        item.setThumbnail(link.getThumbnail());
        item.setTitle(link.getTitle());
        item.setDomain(link.getDomain());
        item.setText(link.getSelftext());
        item.setHtmlText(link.getSelftextHtml());
        item.setNumComments(link.getNumComments());
        item.setIsSelf(link.isSelf());

        item.setIsAdult(link.isAdult());
        item.setIsDistinguished(link.isDistinguished());
        item.setSubreddit(link.getSubreddit());

        item.setType(item.identifyType());

        return item;
    }

    private SubredditItem mapToSubredditItem(RedditSubreddit item) {
        SubredditItem subredditItem = new SubredditItem();
        subredditItem.setName(item.getDisplayName());
        return subredditItem;
    }



}
