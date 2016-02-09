package com.matie.redgram.data.models.main.items.comment;

import java.util.List;

/**
 * Created by matie on 2016-02-08.
 */
public class CommentMoreItem extends CommentBaseItem {
    private int count;
    private String name;
    private List<String> children;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildren() {
        return children;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }
}
