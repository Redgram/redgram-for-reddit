package com.matie.redgram.data.network.api.reddit;

import android.support.annotation.Nullable;

import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.api.reddit.RedditLink;
import com.matie.redgram.data.models.api.reddit.RedditListing;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.main.reddit.PostItemWrapper;
import com.matie.redgram.data.network.api.reddit.base.RedditProviderBase;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.data.network.connection.ConnectionStatus;
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

    public Observable<PostItemWrapper> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params) {

       Observable<RedditResponse<RedditListing>> subObservable = null;

       //todo: check if it belongs to a set of fixed filters (i.e hot,new,top..etc)
       if(filter != null){
           subObservable = provider.getSubreddit(query, filter, params);
       }else{
           subObservable = provider.getSubreddit(query, params);
       }

        return getDefaultListingObservable(subObservable);
    }

    public Observable<PostItemWrapper> executeSearch(String subreddit, @Nullable Map<String, String> params) {

        Observable<RedditResponse<RedditListing>> searchObservable = null;

        if(!subreddit.isEmpty() && subreddit != null){
            params.put("restrict_sr", "true");
            searchObservable = provider.executeSearch(subreddit, params);
        }else{
            //at least have a "q" param set
            searchObservable = provider.executeSearch(params);
        }

        return getDefaultListingObservable(searchObservable);
    }


    public Observable<PostItemWrapper> getListing(String front, @Nullable Map<String, String> params){
        Observable<RedditResponse<RedditListing>> listingObservable = provider.getListing(front, params);
        return getDefaultListingObservable(listingObservable);
    }


    private Observable<PostItemWrapper> getDefaultListingObservable(Observable<RedditResponse<RedditListing>> listingObservable) {

        Observable<List<PostItem>> itemsObservable = getItemsObservable(listingObservable);
        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(listingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, new Func2<List<PostItem>, Map<String, String>, PostItemWrapper>() {
            @Override
            public PostItemWrapper call(List<PostItem> postItems, Map<String, String> map) {
                PostItemWrapper postItemWrapper = new PostItemWrapper();
                postItemWrapper.setBefore(map.get(BEFORE));
                postItemWrapper.setAfter(map.get(AFTER));
                postItemWrapper.setModHash(map.get(MODHASH));
                postItemWrapper.setItems(postItems);
                return postItemWrapper;
            }
        });
    }


    private Observable<Map<String,String>> getFieldsObservable(Observable<RedditResponse<RedditListing>> responseObservable) {
        return responseObservable.map(listing -> mapFieldsToHashMap(listing));
    }

    private Observable<List<PostItem>> getItemsObservable(Observable<RedditResponse<RedditListing>> observable){

        return observable.flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditLink.class)
                .map(link -> mapLinkToPostItem(link))
                .concatMap(postItem -> {

                    //todo: request API
                    Observable<PostItem> imgurObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.IMGUR_IMAGE);
                    //todo: convert to MP4 and set new link
                    Observable<PostItem> mp4Observable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.GIF);
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
                                    || item.getType() == PostItem.Type.IMGUR_CUSTOM_GALLERY
                                    || item.getType() == PostItem.Type.IMGUR_TAG
                                    || item.getType() == PostItem.Type.IMGUR_SUBREDDIT);
                    //todo: display thumbnail with link to view full source along with ant self text
                    Observable<PostItem> defaultObservable = Observable.just(postItem)
                            .filter(item -> item.getType() == PostItem.Type.DEFAULT);

                    return Observable.merge(imageObservable, imgurObservable, mp4Observable, galleryObservable,
                            selfObservable, defaultObservable);
                })
                .toList();
    }

    private Map<String, String> mapFieldsToHashMap(RedditResponse<RedditListing> listing) {
        Map<String, String> map = new HashMap<String,String>();
        RedditListing listingData = (RedditListing)listing.getData();

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
        item.setNumComments(link.getNumComments());
        item.setIsSelf(link.isSelf());

        item.setIsAdult(link.isAdult());
        item.setIsDistinguished(link.isDistinguished());
        item.setSubreddit(link.getSubreddit());

        item.setType(item.adjustType());

        return item;
    }



}
