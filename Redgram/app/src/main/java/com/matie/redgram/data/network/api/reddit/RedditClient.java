package com.matie.redgram.data.network.api.reddit;

import android.app.Application;
import android.support.annotation.Nullable;

import com.matie.redgram.data.managers.rxbus.RxBus;
import com.matie.redgram.data.models.PostItem;
import com.matie.redgram.data.models.events.SubredditEvent;
import com.matie.redgram.data.models.reddit.RedditLink;
import com.matie.redgram.data.models.reddit.RedditListing;
import com.matie.redgram.data.models.reddit.base.RedditResponse;
import com.matie.redgram.data.network.api.reddit.base.RedditProviderBase;
import com.matie.redgram.data.network.api.reddit.base.RedditServiceBase;
import com.matie.redgram.data.network.connection.ConnectionStatus;
import com.matie.redgram.ui.App;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by matie on 17/04/15.
 */
public class RedditClient extends RedditServiceBase {

    private final RedditProviderBase provider;

    @Inject
    public RedditClient(App app, ConnectionStatus status) {
        super(app,status);

        provider = getRestAdapter().create(RedditProviderBase.class);
    }

    public Observable<PostItem> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params) {

       Observable<RedditResponse<RedditListing>> subObservable = null;

       //todo: check if it belongs to a set of fixed filters (i.e hot,new,top..etc)
       if(filter != null){
           subObservable = provider.getSubreddit(query, filter, params);
       }else{
           subObservable = provider.getSubreddit(query, params);
       }

        return subObservable
                .flatMap(response -> Observable.from(response.getData().getChildren()))
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
                });
    }

    public Observable<PostItem> executeSearch(String subreddit, @Nullable Map<String, String> params) {

        Observable<RedditResponse<RedditListing>> searchObservable = null;

        //todo: client should only be responsible for passing parameters to network calls.
        if(!subreddit.isEmpty() && subreddit != null){
            params.put("restrict_sr", "true");
            searchObservable = provider.executeSearch(subreddit, params);
        }else{
            //at least have a "q" param set
            searchObservable = provider.executeSearch(params);
        }

        return searchObservable
                .flatMap(response -> Observable.from(response.getData().getChildren()))
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
                });
    }





    public PostItem mapLinkToPostItem(RedditLink link){

        PostItem item = new PostItem();

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
