package com.matie.redgram.data.models.db;

import io.realm.RealmObject;

/**
 * Global App settings such as appearance and such
 *
 * Created by matie on 2016-02-26.
 */
public class Settings extends RealmObject{
    private boolean isNsfw;

    public boolean isNsfw() {
        return isNsfw;
    }

    public void setIsNsfw(boolean isNsfw) {
        this.isNsfw = isNsfw;
    }
}
