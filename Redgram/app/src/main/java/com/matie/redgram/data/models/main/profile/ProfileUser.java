package com.matie.redgram.data.models.main.profile;


import com.matie.redgram.data.models.main.base.BaseModel;

import org.joda.time.DateTime;

public class ProfileUser extends BaseModel {
    private String id;
    private String username;
    private DateTime createdDate;
    private Karma karma;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Karma getKarma() {
        return karma;
    }

    public void setKarma(Karma karma) {
        this.karma = karma;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
