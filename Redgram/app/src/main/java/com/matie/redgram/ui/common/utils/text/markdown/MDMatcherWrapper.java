package com.matie.redgram.ui.common.utils.text.markdown;

import java.util.List;

/**
 * Created by matie on 2017-02-09.
 */
public class MDMatcherWrapper {
    private int start;
    private int end;
    private List<String> groups;

    public MDMatcherWrapper(int start, int end, List<String> groups) {
        this.start = start;
        this.end = end;
        this.groups = groups;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
