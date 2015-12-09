package com.matie.redgram.ui.common.utils.text;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by matie on 2015-12-08.
 *
 * Responsible for all text manipulation
 */
public class StringFormatter {

    private StringFormatter() {}

    public static Spanned formatFromHtml(String target){
        return Html.fromHtml(target);
    }

    public static String prependRedditLinks(String target){
        target = target.replaceAll("href=\"(/[ru]/[a-zA-Z0-9]+)\"", "href=\"com.matie.redgram://$1\"");
        return target;
    }
}
