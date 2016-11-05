package com.matie.redgram.data.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by matie on 2016-02-26.
 */
public class Session extends RealmObject{
    @PrimaryKey
    private Integer id;
    private User user; //current selected user

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public void cascadeDelete(){
//        // TODO: 2016-05-24 updgrade realm and use deleteFromRealm instead. Also deleteAllFromRealm was added
//        user.getPrefs().removeFromRealm();
//        user.getTokenInfo().removeFromRealm();
//        user.removeFromRealm();
//        removeFromRealm();
//    }
}
