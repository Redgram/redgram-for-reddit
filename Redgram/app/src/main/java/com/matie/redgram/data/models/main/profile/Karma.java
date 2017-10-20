package com.matie.redgram.data.models.main.profile;


import com.matie.redgram.data.models.main.base.BaseModel;

public class Karma extends BaseModel {
    private int link;
    private int comment;

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
