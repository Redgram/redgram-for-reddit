package com.matie.redgram.data.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by matie on 2016-02-25.
 */
public class User extends RealmObject {
    @PrimaryKey
    private String id;
    private String userName;
    private Token tokenInfo;
    //add other stuff like settings and the like

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Token getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(Token tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

}
