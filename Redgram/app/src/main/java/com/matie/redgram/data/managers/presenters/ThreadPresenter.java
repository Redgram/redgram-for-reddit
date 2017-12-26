package com.matie.redgram.data.managers.presenters;

import com.matie.redgram.data.managers.presenters.base.BasePresenter;
import com.matie.redgram.data.models.main.items.submission.PostItem;

import java.util.Map;

public interface ThreadPresenter extends BasePresenter {
    void getThread(String id, Map<String, String> params);
    void vote(PostItem item, int dir);
    void save(PostItem postItem, boolean save);
    void hide(PostItem postItem, boolean hide);
}
