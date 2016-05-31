package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Created by matie on 2016-03-01.
 */
public class AuthPrefs {
    //alter results from the servers, so they have to remain consistent with the user reddit preferences
    private boolean hide_downs;
    private boolean hide_ups;
    private boolean label_nsfw;
    private boolean over_18;
    //passed as parameters or UI rendering
    private String default_comment_sort;
    private boolean show_trending;
    private boolean show_flair;
    private boolean show_link_flair;
    private boolean store_visits;
    private int min_comment_score;
    private int min_link_score;
    private int num_comments;
    private int numsites;
    private String media; //one of on, off, subreddit
    private boolean highlight_controversial;
    private boolean ignore_suggested_sort;

    public int getNumSites() {
        return numsites;
    }

    public String getDefaultCommentSort() {
        return default_comment_sort;
    }

    public boolean isHideDowns() {
        return hide_downs;
    }

    public boolean isHideUps() {
        return hide_ups;
    }

    public boolean isLabelNsfw() {
        return label_nsfw;
    }

    public boolean isOver18() {
        return over_18;
    }

    public boolean isShowTrending() {
        return show_trending;
    }

    public boolean isShowFlair() {
        return show_flair;
    }

    public boolean isShowLinkFlair() {
        return show_link_flair;
    }

    public boolean isStoreVisits() {
        return store_visits;
    }

    public int getMinCommentScore() {
        return min_comment_score;
    }

    public int getMinLinkScore() {
        return min_link_score;
    }

    public int getNumComments() {
        return num_comments;
    }

    public void setOver18(boolean over18) {
        this.over_18 = over18;
    }

    public void setNumsites(int numsites) {
        this.numsites = numsites;
    }

    public void setNumComments(int num_comments) {
        this.num_comments = num_comments;
    }

    public void setMinLinkScore(int min_link_score) {
        this.min_link_score = min_link_score;
    }

    public void setMinCommentScore(int min_comment_score) {
        this.min_comment_score = min_comment_score;
    }

    public void setStoreVisits(boolean store_visits) {
        this.store_visits = store_visits;
    }

    public void setShowLinkFlair(boolean show_link_flair) {
        this.show_link_flair = show_link_flair;
    }

    public void setShowFlair(boolean show_flair) {
        this.show_flair = show_flair;
    }

    public void setShowTrending(boolean show_trending) {
        this.show_trending = show_trending;
    }

    public void setLabelNsfw(boolean label_nsfw) {
        this.label_nsfw = label_nsfw;
    }

    public void setHideUps(boolean hide_ups) {
        this.hide_ups = hide_ups;
    }

    public void setHideDowns(boolean hide_downs) {
        this.hide_downs = hide_downs;
    }

    public void setDefaultCommentSort(String default_comment_sort) {
        this.default_comment_sort = default_comment_sort;
    }


    public boolean getIgnoreSuggestedSort() {
        return ignore_suggested_sort;
    }

    public void setIgnoreSuggestedSort(boolean ignore_suggested_sort) {
        this.ignore_suggested_sort = ignore_suggested_sort;
    }

    public boolean isHighlightControversial() {
        return highlight_controversial;
    }

    public void setHighlightControversial(boolean highlight_controversial) {
        this.highlight_controversial = highlight_controversial;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    //only explicit preferences used as API calls' parameters or which explicitly affect the UI
    public void setToDefault() {
        show_trending = true;
        store_visits = true;
        numsites = 25;
        num_comments = 200;
        min_comment_score = -4;
        min_link_score = -4;
        default_comment_sort = "best";
        show_link_flair = true;
        show_flair = true;
        highlight_controversial = true;
        ignore_suggested_sort = true;
        over_18 = false;
        media = "subreddit"; //maybe implicit
    }

    //    {
//        "beta": boolean value,
//        "clickgadget": boolean value,
//        "collapse_read_messages": boolean value,
//        "compress": boolean value,
//        "creddit_autorenew": boolean value,
//        "default_comment_sort": one of (`confidence`, `old`, `top`, `qa`, `controversial`, `new`),
//        "domain_details": boolean value,
//        "email_messages": boolean value,
//        "enable_default_themes": boolean value,
//        "hide_ads": boolean value,
//        "hide_downs": boolean value,
//        "hide_from_robots": boolean value,
//        "hide_locationbar": boolean value,
//        "hide_ups": boolean value,
//        "highlight_controversial": boolean value,
//        "highlight_new_comments": boolean value,
//        "ignore_suggested_sort": boolean value,
//        "label_nsfw": boolean value,
//        "lang": a valid IETF language tag (underscore separated),
//            "legacy_search": boolean value,
//        "mark_messages_read": boolean value,
//        "media": one of (`on`, `off`, `subreddit`),
//        "min_comment_score": an integer between -100 and 100,
//            "min_link_score": an integer between -100 and 100,
//            "monitor_mentions": boolean value,
//        "newwindow": boolean value,
//        "no_profanity": boolean value,
//        "num_comments": an integer between 1 and 500,
//        "numsites": an integer between 1 and 100,
//        "organic": boolean value,
//        "other_theme": subreddit name,
//        "over_18": boolean value,
//        "private_feeds": boolean value,
//        "public_votes": boolean value,
//        "research": boolean value,
//        "show_flair": boolean value,
//        "show_gold_expiration": boolean value,
//        "show_link_flair": boolean value,
//        "show_promote": boolean value,
//        "show_stylesheets": boolean value,
//        "show_trending": boolean value,
//        "store_visits": boolean value,
//        "theme_selector": subreddit name,
//        "threaded_messages": boolean value,
//        "threaded_modmail": boolean value,
//        "use_global_defaults": boolean value,
//    }

}
