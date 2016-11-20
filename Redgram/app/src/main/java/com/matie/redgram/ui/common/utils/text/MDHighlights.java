package com.matie.redgram.ui.common.utils.text;

import java.util.regex.Pattern;

/**
 * Created by matie on 2015-12-06.
 *
 * Represents MarkDown in text
 */
public enum MDHighlights {

    LINK(Pattern.compile("\\[([^\\[]+)\\]\\(([^\\)]+)\\)")),
    HEADER(Pattern.compile("(((\\n|^)#+.*?\\n)|((\\n|^).*?\\n(-|=)+))")),
    STRIKE(Pattern.compile("\\~\\~(.*?)\\~\\~")),
    BOLD(Pattern.compile("(\\*\\*|__)(.*?)\\1")),
    ITALICS(Pattern.compile("(\\*|_)(.*?)\\1")),
    SUB(Pattern.compile("/?r/[a-zA-Z0-9]+")),
    USER(Pattern.compile("/?u/[a-zA-Z0-9]+"));

    private Pattern pattern;

    MDHighlights(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
