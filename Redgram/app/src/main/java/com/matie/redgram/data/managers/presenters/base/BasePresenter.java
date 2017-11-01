package com.matie.redgram.data.managers.presenters.base;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;

/**
 * Created by matie on 2016-09-25.
 */
public interface BasePresenter {
    void registerForEvents();
    void unregisterForEvents();
    DatabaseManager databaseManager();
}
