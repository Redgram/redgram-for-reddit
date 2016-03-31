package com.matie.redgram.data.models.api.reddit.auth;

/**
 * Created by matie on 2016-03-01.
 */
public class AuthPrefs {

    private String default_comment_sort;
    private boolean hide_downs;
    private boolean hide_ups;
    private boolean label_nsfw;
    private boolean over_18;
    private boolean show_trending;
    private boolean show_flair;
    private boolean show_link_flair;
    private boolean store_visits;
    private int min_comment_score;
    private int min_link_score;
//    private String media; // TODO: 2016-03-01
    private int num_comments;

    public String getDefault_comment_sort() {
        return default_comment_sort;
    }

    public boolean isHide_downs() {
        return hide_downs;
    }

    public boolean isHide_ups() {
        return hide_ups;
    }

    public boolean isLabel_nsfw() {
        return label_nsfw;
    }

    public boolean isOver_18() {
        return over_18;
    }

    public boolean isShow_trending() {
        return show_trending;
    }

    public boolean isShow_flair() {
        return show_flair;
    }

    public boolean isShow_link_flair() {
        return show_link_flair;
    }

    public boolean isStore_visits() {
        return store_visits;
    }

    public int getMin_comment_score() {
        return min_comment_score;
    }

    public int getMin_link_score() {
        return min_link_score;
    }

    public int getNum_comments() {
        return num_comments;
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
//            "numsites": an integer between 1 and 100,
//            "organic": boolean value,
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
