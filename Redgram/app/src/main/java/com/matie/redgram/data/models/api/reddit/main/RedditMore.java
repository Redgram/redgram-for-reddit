package com.matie.redgram.data.models.api.reddit.main;
import com.matie.redgram.data.models.api.reddit.base.RedditObject;

import java.util.List;

/**
 * Created by matie on 2016-02-08.
 */
public class RedditMore extends RedditObject{
    int count;
    String parent_id;
    String name;
    String id;
    List<String> children;

    public int getCount() {
        return count;
    }

    public String getParentId() {
        return parent_id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<String> getChildren() {
        return children;
    }
}
