package com.matie.redgram.data.models.db;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by matie on 2016-02-26.
 */
public class Session extends RealmObject{
    @PrimaryKey
    private Integer id;
    private User user; //current selected user
    private String recentPost;

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

    public String getRecentPost() {
        return recentPost;
    }

    public void setRecentPost(String recentPost) {
        this.recentPost = recentPost;
    }
}
