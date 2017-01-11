package com.matie.redgram.ui.common.utils.text;

/**
 * Created by matie on 2016-11-19.
 */
public class MDStyle {
    private int textColor;
    private int headerColor;
    private int linkColor;
    private int codeTextColor;
    private int codeBackgroundColor;

    public MDStyle(int textColor, int headerColor, int linkColor, int codeTextColor, int codeBackgroundColor) {
        this.textColor = textColor;
        this.headerColor = headerColor;
        this.linkColor = linkColor;
        this.codeTextColor = codeTextColor;
        this.codeBackgroundColor = codeBackgroundColor;
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

    public int getCodeTextColor() {
        return codeTextColor;
    }

    public void setCodeTextColor(int codeTextColor) {
        this.codeTextColor = codeTextColor;
    }

    public int getCodeBackgroundColor() {
        return codeBackgroundColor;
    }

    public void setCodeBackgroundColor(int codeBackgroundColor) {
        this.codeBackgroundColor = codeBackgroundColor;
    }
}
