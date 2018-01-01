package com.matie.redgram.data.network.api.reddit.user;

import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;
import com.matie.redgram.data.models.api.reddit.auth.AuthUser;
import com.matie.redgram.data.models.api.reddit.auth.AuthWrapper;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;
import com.matie.redgram.data.models.api.reddit.base.RedditResponse;
import com.matie.redgram.data.models.api.reddit.main.RedditComment;
import com.matie.redgram.data.models.api.reddit.main.RedditLink;
import com.matie.redgram.data.models.api.reddit.main.RedditListing;
import com.matie.redgram.data.models.api.reddit.main.RedditMore;
import com.matie.redgram.data.models.api.reddit.main.RedditSubreddit;
import com.matie.redgram.data.models.api.reddit.main.RedditUser;
import com.matie.redgram.data.models.db.User;
import com.matie.redgram.data.models.main.base.BaseModel;
import com.matie.redgram.data.models.main.base.Listing;
import com.matie.redgram.data.models.main.items.submission.PostItem;
import com.matie.redgram.data.models.main.items.SubredditItem;
import com.matie.redgram.data.models.main.items.UserItem;
import com.matie.redgram.data.models.main.items.submission.SubmissionItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentBaseItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentMoreItem;
import com.matie.redgram.data.models.main.items.submission.comment.CommentsWrapper;
import com.matie.redgram.data.network.api.reddit.base.RedditService;
import com.matie.redgram.ui.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class RedditClient extends RedditService implements RedditClientInterface {
    public static final String BEFORE = "before";
    public static final String AFTER = "after";
    public static final String MODHASH = "modhash";

    private final RedditProvider provider;

    @Inject
    public RedditClient(App app) {
        super(app);

        this.provider = buildRetrofit(OAUTH_HOST_ABSOLUTE).create(RedditProvider.class);
    }

    @Override
    public Observable<AuthWrapper> getAuthWrapper(String code){
        return getAccessToken(code)
                .filter(accessToken -> accessToken.getAccessToken() != null) //make sure it's not null
                .flatMap(accessToken -> {
                    String token = accessToken.getAccessToken();
                    return Observable.zip(getUser(token), getUserPrefs(token), Observable.just(accessToken),
                        (authUser, authPrefs, accessToken1) -> {
                            AuthWrapper wrapper = new AuthWrapper();
                            wrapper.setAccessToken(accessToken1);
                            wrapper.setAuthUser(authUser);
                            wrapper.setAuthPrefs(authPrefs);
                            wrapper.setType(User.USER_AUTH);
                            return wrapper;
                    });
                });
    }

    @Override
    public Observable<AuthWrapper> getAuthWrapper(){
        return getAccessTokenObservable()
                .filter(accessToken -> accessToken.getAccessToken() != null) //make sure it's not null
                .map(accessToken -> {
                    AuthWrapper wrapper = new AuthWrapper();
                    wrapper.setAccessToken(accessToken);
                    wrapper.setType(User.USER_GUEST);
                    return wrapper;
                });
    }

    @Override
    public Observable<AuthUser> getUser(@Nullable String accessToken) {
        accessToken = "bearer " + accessToken;
        return provider.getAuthUser(accessToken);
    }

    @Override
    public Observable<AuthPrefs> getUserPrefs(@Nullable String accessToken){
        accessToken = "bearer " + accessToken;
        return provider.getUserPrefs(accessToken);
    }

    @Override
    public Observable<AuthPrefs> updatePrefs(AuthPrefs prefs) {
        return provider.updatePrefs(prefs);
    }

    @Override
    public Observable<Listing<UserItem>> getFriends(){
        Observable<RedditObject>
                friendsObservable = provider.getFriends();
        return getDefaultUserListObservable(friendsObservable);
    }

    @Override
    public Observable<Listing<UserItem>> getBlockedUsers(){
        Observable<RedditObject>
                blockedUsersObservable = provider.getBlockedUsers();
        return getDefaultUserListObservable(blockedUsersObservable);
    }

    /**
     * TODO: 10/18/17 create a service for each category of API calls (profile, thread, etc and let the service
     * TODO: 10/18/17  services should return the UI model instead of network model
     */
    @Override
    public Observable<RedditUser> getUserDetails(String username){
        return provider.getUserDetails(username).map(RedditResponse::getData);
    }

    @Override
    public Observable<Listing<BaseModel>> getUserOverview(String username){
        Observable<RedditResponse<RedditListing>>
                userOverview = provider.getUserOverview(username);
        return null;
    }

    @Override
    public Observable<Listing<PostItem>> getSubredditListing(String query, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<RedditListing>> subObservable
                = provider.getSubreddit(query, params);

        return getDefaultPostListObservable(subObservable, postItems);

    }

    @Override
    public Observable<Listing<PostItem>> getSubredditListing(String query, @Nullable String filter, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<RedditListing>> subObservable
                = provider.getSubreddit(query, filter, params);

        return getDefaultPostListObservable(subObservable, postItems);
    }

    @Override
    public Observable<Listing<PostItem>> executeSearch(String subreddit, @Nullable Map<String, String> params, List<PostItem> postItems) {

        Observable<RedditResponse<RedditListing>> searchObservable;

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

    @Override
    public Observable<Listing<PostItem>> getListing(String front, @Nullable Map<String, String> params, List<PostItem> postItems){
        Observable<RedditResponse<RedditListing>> listingObservable = provider.getListing(front, params);
        return getDefaultPostListObservable(listingObservable, postItems);
    }

    @Override
    public Observable<Listing<SubredditItem>> getSubreddits(String filter, @Nullable Map<String, String> params){

        Observable<RedditResponse<RedditListing>> subredditsListingObservable
                = provider.getSubredditsListing(filter, params);

        return getSubredditsObservable(subredditsListingObservable);
    }

    @Override
    public Observable<Listing<SubredditItem>> getSubscriptions(@Nullable Map<String, String> params){

        Observable<RedditResponse<RedditListing>> subredditsListingObservable
                = provider.getSubscriptions(params);

        return getSubredditsObservable(subredditsListingObservable);
    }

    @Override
    public Observable<CommentsWrapper> getCommentsByArticle(String article, @Nullable Map<String, String> params){

        Observable<List<RedditResponse<RedditListing>>>
                commentsWrapper = provider.getCommentsByArticle(article, params);

        Observable<RedditResponse<RedditListing>> listings =
                commentsWrapper.flatMapIterable(redditResponses -> redditResponses);

        Observable<PostItem>
                postObservable = listings
                                    .first() //first listing is the post
                                    .flatMap(data -> Observable.from(data.getData().getChildren()))
                                    .cast(RedditLink.class)
                                    .map(this::mapLinkToPostItem);

        Observable<List<SubmissionItem>>
                redditCommentObjects = listings
                                        .last() //second listing are the comments
                                        .flatMap(data -> Observable.from(data.getData().getChildren()))
                                        .concatMap(comment -> {

                                            List<SubmissionItem> comments = new ArrayList<>();

                                            if (comment instanceof RedditComment) {
                                                mapCommentToCommentItem((RedditComment) comment, 0, comments);
                                            } else if (comment instanceof RedditMore) {
                                                mapCommentToCommentMoreItem((RedditMore) comment, 0, comments);
                                            }

                                            return Observable.from(comments);
                                        })
                                        .filter(finalItem -> (finalItem != null))
                                        .toList();


        return Observable.zip(postObservable, redditCommentObjects, (item, commentBaseItems) -> {
            CommentsWrapper commentsWrapper1 = new CommentsWrapper();
            commentsWrapper1.setPostItem(item);
            commentsWrapper1.setCommentItems(commentBaseItems);
            return commentsWrapper1;
        });

    }

    @Override
    public Observable<JsonElement> voteFor(String id, Integer direction){
        return provider.voteFor(id, direction);
    }

    @Override
    public Observable<JsonElement> hide(String id, boolean hide){
        if(hide)
            return provider.hide(id);
        else
            return provider.unhide(id);
    }

    @Override
    public Observable<JsonElement> save(String id, boolean save){
        if(save)
            return provider.save(id);
        else
            return provider.unsave(id);
    }

    //Confirmation prompt
    @Override
    public Observable<JsonElement> report(String id){
        return provider.report("json", id, "");
    }

    @Override
    public Observable<JsonElement> delete(String id){
        return provider.delete(id);
    }

    //helpers / builders / transformers
    private Observable<Listing<UserItem>> getDefaultUserListObservable(Observable<RedditObject> listing) {
        Observable<RedditListing> redditListingObservable =
                listing.map(redditObject -> (RedditListing)redditObject);

        Observable<List<UserItem>> listObservable = redditListingObservable
                .flatMap(data -> Observable.from(data.getChildren()))
                .cast(RedditUser.class)
                .map(redditUser -> new UserItem(redditUser.getId(), redditUser.getName()))
                .toList();

        Observable<Map<String, String>> fieldsObservable = redditListingObservable.map(this::buildFieldsMap);

        return Observable.zip(listObservable, fieldsObservable, (userItems, fields) -> {
            Listing<UserItem> userListing = new Listing<UserItem>();
            userListing.setItems(userItems);
            userListing.setAfter(fields.get(AFTER));
            userListing.setBefore(fields.get(BEFORE));
            userListing.setModHash(fields.get(MODHASH));
            return userListing;
        });
    }

    private Observable<Listing<PostItem>> getDefaultPostListObservable(Observable<RedditResponse<RedditListing>> listingObservable, List<PostItem> postItems) {

        Observable<List<PostItem>> itemsObservable = getItemsObservable(listingObservable, postItems);
        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(listingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, (postItems1, map) -> {
            Listing<PostItem> postItemWrapper = new Listing();
            postItemWrapper.setBefore(map.get(BEFORE));
            postItemWrapper.setAfter(map.get(AFTER));
            postItemWrapper.setModHash(map.get(MODHASH));
            postItemWrapper.setItems(postItems1);
            return postItemWrapper;
        });
    }

    private Observable<List<PostItem>> getItemsObservable(Observable<RedditResponse<RedditListing>> observable, @Nullable List<PostItem> postItems){

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

    private Observable<Listing<SubredditItem>> getSubredditsObservable(Observable<RedditResponse<RedditListing>> subredditsListingObservable) {
        Observable<List<SubredditItem>> itemsObservable = subredditsListingObservable
                .filter(response -> response != null)
                .flatMap(response -> Observable.from(response.getData().getChildren()))
                .cast(RedditSubreddit.class)
                .map(this::mapToSubredditItem)
                .toList();

        Observable<Map<String,String>> fieldsObservable = getFieldsObservable(subredditsListingObservable);

        return Observable.zip(itemsObservable, fieldsObservable, (items, map) -> {
            Listing<SubredditItem> postItemWrapper = new Listing();
            postItemWrapper.setBefore(map.get(BEFORE));
            postItemWrapper.setAfter(map.get(AFTER));
            postItemWrapper.setModHash(map.get(MODHASH));
            postItemWrapper.setItems(items);
            return postItemWrapper;
        });
    }
    /**
     * Maps attributes that belong to listings only. BEFORE AFTER AND MODHASH.
     * @param responseObservable
     * @return
     */
    private Observable<Map<String,String>> getFieldsObservable(Observable<RedditResponse<RedditListing>> responseObservable) {
        return responseObservable.map(this::mapFieldsToHashMap);
    }

    private Map<String, String> mapFieldsToHashMap(RedditResponse<RedditListing> listing) {
        return buildFieldsMap(listing.getData());
    }

    private Map<String, String> buildFieldsMap(RedditListing listingData) {
        Map<String, String> map = new HashMap<String,String>();

        map.put(AFTER, listingData.getAfter());
        map.put(BEFORE, listingData.getBefore());
        map.put(MODHASH, listingData.getModhash());

        return map;
    }

    private int mapCommentToCommentItem(RedditComment commentData, int level, List<SubmissionItem> comments) {
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

        if(commentData.getReplies() instanceof RedditListing) {
            com.matie.redgram.data.models.api.reddit.main.RedditListing listing =
                    (RedditListing) commentData.getReplies();
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

    private void mapCommentToCommentMoreItem(RedditMore commentData, int level, List<SubmissionItem> comments) {
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

    /**
     * This function replaces the old item in the list with the new one only if both items have the
     * same id. If replaced, filter out the emitted item. If not, return true to include it.
     *
     * Note: equals method of PostItem is overridden to only return true if ID are the same.
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
}
