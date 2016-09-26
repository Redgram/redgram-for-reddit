package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;
import com.matie.redgram.data.models.api.reddit.auth.AuthPrefs;

import java.util.Map;

/**
 * Links Presenter Interface.
 */
public interface LinksPresenter extends BasePresenter{
    void getListing(String subreddit, String front, Map<String,String> params);
    void getMoreListing(String subreddit, String front, Map<String,String> params);
    void searchListing(String subreddit, Map<String,String> params);
    void voteFor(int position, String name, Integer dir);
    void hide(int position, String name, boolean showUndo);
    void unHide();
    void save(int position, String name, boolean save);
    void delete(int position);
    void report(int position);
    //dealing with settings
    void confirmAge();
    void enableNsfwPreview();
}
