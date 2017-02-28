package com.matie.redgram.ui.common.utils.text.markdown;

import java.util.regex.Pattern;

/**
 * Created by matie on 2015-12-06.
 *
 * Represents MarkDown in text
 */
public enum MDHighlights {

    //consider escapes in special characters
    LINK(Pattern.compile("\\[([^\\[]+)\\]\\(((https?|ftp|mailto|steam|irc|news|mumble|ssh):\\/\\/[^\\)]+)\\)")),
    FULL_LINK(Pattern.compile("((https?|ftp|mailto:|steam|irc|news|mumble|ssh):?\\/\\/|w{3}\\.)[^\\s]+\\.[^\\s]+")),
    HEADER(Pattern.compile("(((\\n|^)#+.*?\\n)|((\\n|^).*?\\n(-|=)+))")),
    UNORDERED_LIST(Pattern.compile("((\\n|^)(( {1,}|\\t)?)((\\\\?)(\\*|\\+|-) +\\n?(.*)))")),
    ORDERED_LIST(Pattern.compile("((\\n|^)( {1,}|\\t)?((\\\\?)([1-9]+.) +\\n?(.*)))")),
    SEPARATOR(Pattern.compile("((?<![^\\n])\\n*?\\*{5,}(?![^\\n]))")),
    SUPERSCRIPT(Pattern.compile("(?<=[^\\s\\^`])\\^(\\(([^\\^\\)]*)\\)|[^\\s\\^]*)")),
    STRIKE(Pattern.compile("(\\~\\~)(.*?)\\1")),
    BOLD(Pattern.compile("(\\*\\*|__)(.*?)\\1")),
    ITALIC(Pattern.compile("(\\*|_)(.*?)\\1")),
    CODE(Pattern.compile("(`)(.*?)\\1")),
    CODE_BLOCK(Pattern.compile("(( {4}|\\t)(.*?(\\n|$)))+")),
    BLOCK_QUOTE(Pattern.compile("((?<![^\\n])\\n*?(\\\\?)(>+)(.*))")),
    SUB_USER(Pattern.compile("(?<!(\\\\|\\w))\\/(r|u)\\/[a-zA-Z0-9][\\w][\\w\\/]*|(?<!(\\/|\\w))(r|u)\\/[a-zA-Z0-9][\\w][\\w\\/]*"));

    private Pattern pattern;

    MDHighlights(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
