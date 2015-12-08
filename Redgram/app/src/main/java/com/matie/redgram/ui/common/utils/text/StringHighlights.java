package com.matie.redgram.ui.common.utils.text;

import java.util.regex.Pattern;

/**
 * Created by matie on 2015-12-06.
 */
public enum StringHighlights {

    LINK(Pattern.compile("\\[([^\\[]+)\\]\\(([^\\)]+)\\)")),
    HEADER(Pattern.compile("(((\\n|^)#+.*?\\n)|((\\n|^).*?\\n(-|=)+))")),
    STRIKE(Pattern.compile("\\~\\~(.*?)\\~\\~")),
    BOLD(Pattern.compile("(\\*\\*|__)(.*?)\\1")),
    ITALICS(Pattern.compile("(\\*|_)(.*?)\\1"));

    private Pattern pattern;

    StringHighlights(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
