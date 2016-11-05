package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;

import java.util.List;

/**
 * Home Presenter Interface
 */
public interface HomePresenter extends BasePresenter{
    void getHomeViewWrapper();
    List<String> getSubreddits();
}
