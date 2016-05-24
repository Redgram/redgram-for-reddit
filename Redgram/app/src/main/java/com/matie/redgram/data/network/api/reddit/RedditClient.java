package com.matie.redgram.data.network.api.reddit;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AccessToken;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.api.reddit.main.RedditComment;
import com.matie.redgram.data.models.api.reddit.main.RedditMore;
import com.matie.redgram.data.models.api.reddit.main.RedditSubreddit;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.db.Prefs;
import com.matie.redgram.data.models.main.items.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.PostItem;
import com.matie.redgram.data.models.api.reddit.main.RedditLink;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.comment.CommentItem;
import com.matie.redgram.data.models.main.items.comment.CommentMoreItem;
import com.matie.redgram.data.models.main.items.comment.CommentsWrapper;
import com.matie.redgram.data.models.main.reddit.RedditListing;
import com.matie.redgram.data.network.api.reddit.base.RedditProvider;
import com.matie.redgram.data.network.api.reddit.base.RedditService;
import com.matie.redgram.ui.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * Created by matie on 17/04/15.
 */
public class RedditClient extends RedditService {
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String MODHASH = "modhash";

    private final RedditProvider provider;

    @Inject
    public RedditClient(App app) {
        super(app);
        this.provider = buildRetrofit(OAUTH_HOST_ABSOLUTE).create(RedditProvider.class);
    }

    public Observable<AuthWrapper> getAuthWrapper(String code){
        return getAccessToken(code)
                .filter(accessToken -> accessToken.getAccessToken() != null) //make sure it's not null
                .flatMap(accessToken -> {
                    String token = accessToken.getAccessToken();
                    return Observable.zip(getUser(token), getUserPrefs(token), Observable.just(accessToken), new Func3<AuthUser, AuthPrefs, AccessToken, AuthWrapper>() {
                        @Override
                        public AuthWrapper call(AuthUser authUser, AuthPrefs authPrefs, AccessToken accessToken) {
                            AuthWrapper wrapper = new AuthWrapper();
                            wrapper.setAccessToken(accessToken);
                            wrapper.setAuthUser(authUser);
                            wrapper.setAuthPrefs(authPrefs);
                            return wrapper;
                        }
                    });
                });
    }

    public Observable<AuthUser> getUser(@Nullable String accessToken) {
        accessToken = "bearer " + accessToken;
        return provider.getUser(accessToken);
    }

    public Observable<AuthPrefs> getUserPrefs(@Nullable String accessToken){
        accessToken = "bearer " + accessToken;
        return provider.getUserPrefs(accessToken);
    }

    public Observable<AuthPrefs> updatePrefs(AuthPrefs prefs) {
        return provider.updatePrefs(prefs);
    }

    public Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> subObservable
                = provider.getSubreddit(query, params);

        return getDefaultPostListObservable(subObservable, postItems);

    }

    public Observable<RedditListing<PostItem>> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> subObservable
                = provider.getSubreddit(query, filter, params);

        return getDefaultPostListObservable(subObservable, postItems);
    }

    public Observable<RedditListing<PostItem>> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> searchObservable = null;

        if(!subreddit.isEmpty()){
            if (params != null) {
                params.put("restrict_sr", "true");
            }
            searchObservable = provider.executeSearch(subreddit, params);
        }else{
            //at least have a "q" param set
            searchObservable = provider.executeSearch(params);
        }

        return getDefaultPostListObservable(searchObservable, postItems);
    }

    public Observable<RedditListing<PostItem>> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems){
        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> listingObservable = provider.getListing(front, params);
        return getDefaultPostListObservable(listingObservable, postItems);
    }

    private Observable<RedditListing<PostItem>> getDefaultPostListObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> listingObservable, List<PostItem> postItems) {

        Observable<List<PostItem>> itemsObservable = getItemsObservable(listingObservable, postItems);
        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(listingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, new Func2<List<PostItem>, Map<String, String>, RedditListing<PostItem>>() {
            @Override
            public RedditListing<PostItem> call(List<PostItem> postItems, Map<String, String> map) {
                RedditListing<PostItem> postItemWrapper = new RedditListing();
                postItemWrapper.setBefore(map.get(BEFORE));
                postItemWrapper.setAfter(map.get(AFTER));
                postItemWrapper.setModHash(map.get(MODHASH));
                postItemWrapper.setItems(postItems);
                return postItemWrapper;
            }
        });
    }

    private Observable<List<PostItem>> getItemsObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> observable, @Nullable List<PostItem> postItems){

        return observable.flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditLink.class)
                .filter(link -> filterHidden(link)) //filters hidden posts
                .map(this::mapLinkToPostItem)
                .filter(item -> (postItems != null ? filterExistingItems(postItems, item) : true))
                .toList();
    }

    private Boolean filterHidden(RedditLink link) {
        if(link.isHidden()){
            return false;
        }
        return true;
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

    public Observable<RedditListing<SubredditItem>> getSubreddits(String filter, @Nullable Map<String, String> params){

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> subredditsListingObservable
                = provider.getSubredditsListing(filter, params);

        return getSubredditsObservable(subredditsListingObservable);
    }

    public Observable<RedditListing<SubredditItem>> getSubscriptions(@Nullable Map<String, String> params){

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> subredditsListingObservable
                = provider.getSubscriptions(params);

        return getSubredditsObservable(subredditsListingObservable);
    }

    private Observable<RedditListing<SubredditItem>> getSubredditsObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> subredditsListingObservable) {
        Observable<List<SubredditItem>> itemsObservable = subredditsListingObservable
                .flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditSubreddit.class)
                .map(item -> mapToSubredditItem(item))
                .toList();

        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(subredditsListingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, new Func2<List<SubredditItem>, Map<String, String>, RedditListing<SubredditItem>>() {
            @Override
            public RedditListing<SubredditItem> call(List<SubredditItem> items, Map<String, String> map) {
                RedditListing<SubredditItem> postItemWrapper = new RedditListing();
                postItemWrapper.setBefore(map.get(BEFORE));
                postItemWrapper.setAfter(map.get(AFTER));
                postItemWrapper.setModHash(map.get(MODHASH));
                postItemWrapper.setItems(items);
                return postItemWrapper;
            }
        });
    }

    public Observable<CommentsWrapper> getCommentsByArticle(String article, @Nullable Map<String, String> params){

        Observable<List<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>>>
                commentsWrapper = provider.getCommentsByArticle(article, params);

        Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> listings =
                commentsWrapper.flatMapIterable(redditResponses -> redditResponses);

        Observable<PostItem>
                postObservable = listings
                                    .first() //first listing is the post
                                    .flatMap(data -> Observable.from(data.getData().getChildren()))
                                    .cast(RedditLink.class)
                                    .map(postData -> mapLinkToPostItem(postData));

        Observable<List<CommentBaseItem>>
                redditCommentObjects = listings
                                        .last() //second listing are the comments
                                        .flatMap(data -> Observable.from(data.getData().getChildren()))
                                        .concatMap(comment -> {

                                            List<CommentBaseItem> comments = new ArrayList<CommentBaseItem>();

                                            if (comment instanceof RedditComment) {
                                                mapCommentToCommentItem((RedditComment) comment, 0, comments);
                                            } else if (comment instanceof RedditMore) {
                                                mapCommentToCommentMoreItem((RedditMore) comment, 0, comments);
                                            }

                                            return Observable.from(comments);
                                        })
                                        .filter(finalItem -> (finalItem != null))
                                        .toList();


        return Observable.zip(postObservable, redditCommentObjects, new Func2<PostItem, List<CommentBaseItem>, CommentsWrapper>() {
            @Override
            public CommentsWrapper call(PostItem item, List<CommentBaseItem> commentBaseItems) {
                CommentsWrapper commentsWrapper = new CommentsWrapper();
                commentsWrapper.setPostItem(item);
                commentsWrapper.setCommentItems(commentBaseItems);
                return commentsWrapper;
            }
        });

    }

    public Observable<JsonElement> voteFor(String id, Integer direction){
        return provider.voteFor(id, direction);
    }

    public Observable<JsonElement> hide(String id, boolean hide){
        if(hide)
            return provider.hide(id);
        else
            return provider.unhide(id);
    }

    public Observable<JsonElement> save(String id, boolean save){
        if(save)
            return provider.save(id);
        else
            return provider.unsave(id);
    }

    //Confirmation prompt
    public Observable<JsonElement> report(String id){
        return provider.report("json", id, "");
    }

    //Confirmation prompt
    public Observable<JsonElement> delete(String id){
        return provider.delete(id);
    }

    /**
     * Maps attributes that belong to listings only. BEFORE AFTER AND MODHASH.
     * @param responseObservable
     * @return
     */
    private Observable<Map<String,String>> getFieldsObservable(Observable<RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing>> responseObservable) {
        return responseObservable.map(listing -> mapFieldsToHashMap(listing));
    }

    private Map<String, String> mapFieldsToHashMap(RedditResponse<com.matie.redgram.data.models.api.reddit.main.RedditListing> listing) {
        Map<String, String> map = new HashMap<String,String>();
        com.matie.redgram.data.models.api.reddit.main.RedditListing listingData = (com.matie.redgram.data.models.api.reddit.main.RedditListing)listing.getData();

        map.put(AFTER, listingData.getAfter());
        map.put(BEFORE, listingData.getBefore());
        map.put(MODHASH, listingData.getModhash());

        return map;
    }

    private int mapCommentToCommentItem(RedditComment commentData, int level, List<CommentBaseItem> comments) {
        CommentItem item = new CommentItem();
        int count = 0; //child count defaults 0

        item.setCommentType(CommentBaseItem.CommentType.REGULAR);
        item.setId(commentData.getId());
        item.setLinkId(commentData.getLinkId());
        item.setAuthor(commentData.getAuthor());
        item.setBody(commentData.getBody());
        item.setBodyHtml(commentData.getBody_html());
        item.setParentId(commentData.getParentId());
        item.setSubredditId(commentData.getSubredditId());
        item.setEdited(commentData.isEdited());
        item.setLevel(level);

        comments.add(item);
        level += 1;

        if(commentData.getReplies() instanceof com.matie.redgram.data.models.api.reddit.main.RedditListing) {
            com.matie.redgram.data.models.api.reddit.main.RedditListing listing =
                    (com.matie.redgram.data.models.api.reddit.main.RedditListing) commentData.getReplies();
            if(listing.getChildren().size() > 0){
                item.setHasReplies(true);
                count += listing.getChildren().size();
                for (RedditObject object : listing.getChildren()) {
                    if (object instanceof RedditComment) {
                        count += mapCommentToCommentItem((RedditComment)object, level, comments);
                    }else if(object instanceof RedditMore){
                        mapCommentToCommentMoreItem((RedditMore)object, level, comments);
                    }
                }
                item.setChildCount(count);
            }
        }
        return count;
    }

    private void mapCommentToCommentMoreItem(RedditMore commentData, int level, List<CommentBaseItem> comments) {
        CommentMoreItem item = new CommentMoreItem();

        item.setCommentType(CommentBaseItem.CommentType.MORE);
        item.setId(commentData.getId());
        item.setCount(commentData.getCount());
        item.setName(commentData.getName());
        item.setParentId(commentData.getParentId());
        item.setChildren(commentData.getChildren());
        item.setLevel(level);

        comments.add(item);
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
        item.setPermalink(link.getPermalink());
        item.setThumbnail(link.getThumbnail());
        item.setTitle(link.getTitle());
        item.setDomain(link.getDomain());
        item.setText(link.getSelftext());
        item.setLikes(link.getLikes());
        item.setHtmlText(link.getSelftextHtml());
        item.setNumComments(link.getNumComments());
        item.setIsSelf(link.isSelf());
        item.setSaved(link.isSaved());

        item.setIsAdult(link.isAdult());
        item.setIsDistinguished(link.isDistinguished());
        item.setSubreddit(link.getSubreddit());

        item.setType(item.identifyType());

        return item;
    }

    private SubredditItem mapToSubredditItem(RedditSubreddit item) {
        SubredditItem subredditItem = new SubredditItem();
        subredditItem.setName(item.getDisplayName());
        subredditItem.setAccountsActive(item.getAccountsActive());
        subredditItem.setDescription(item.getDescription());
        subredditItem.setDescriptionHtml(item.getDescriptionHtml());
        subredditItem.setSubscribersCount(item.getSubscribers());
        subredditItem.setUrl(item.getUrl());
        subredditItem.setAge(item.isOver18());
        subredditItem.setHeaderImage(item.getHeaderImg());
        subredditItem.setHeaderTitle(item.getHeaderTitle());
        subredditItem.setPublicTraffic(item.isPublicTraffic());
        subredditItem.setSubredditType(item.getSubredditType());
        subredditItem.setSubmissionType(item.getSubmissionType());
        return subredditItem;
    }


}
