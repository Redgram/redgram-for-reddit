package com.matie.redgram.ui.common.utils.text;

/**
 * Created by matie on 2016-11-19.
 */
public class MDStyle {
    private int textColor;
    private int headerColor;
    private int linkColor;

    public MDStyle(int textColor, int headerColor, int linkColor) {
        this.textColor = textColor;
        this.headerColor = headerColor;
        this.linkColor = linkColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(int headerColor) {
        this.headerColor = headerColor;
    }

    public int getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(int linkColor) {
        this.linkColor = linkColor;
    }
}
