package com.matie.redgram.data.managers.presenters.base;

import com.matie.redgram.data.managers.storage.db.DatabaseManager;


public interface BasePresenter {
    void registerForEvents();
    void unregisterForEvents();
    DatabaseManager databaseManager();
}
