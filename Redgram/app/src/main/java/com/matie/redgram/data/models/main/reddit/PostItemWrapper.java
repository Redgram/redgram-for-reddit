package com.matie.redgram.data.models.main.reddit;

import com.matie.redgram.data.models.PostItem;

import java.util.List;

/**
 * Created by matie on 13/09/15.
 */
public class PostItemWrapper {
    private List<PostItem> items;
    private String modHash;
    private String after;
    private String before;

    public List<PostItem> getItems() {
        return items;
    }

    public void setItems(List<PostItem> items) {
        this.items = items;
    }

    public String getModHash() {
        return modHash;
    }

    public void setModHash(String modHash) {
        this.modHash = modHash;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
