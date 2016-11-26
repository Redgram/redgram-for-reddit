package com.matie.redgram.ui.thread.views.widgets.comment;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by teekoo777 on 6.11.2016.
 */
public class CommentRegularItemViewTest {
    CommentRegularItemView v;
    private Context instrumantationCtx;

    @Before
    public void setup() {
        instrumantationCtx = InstrumentationRegistry.getContext();
        v = new CommentRegularItemView(instrumantationCtx);
    }

    @Test
    public void parseLinks_test() {

        String exampleComment1 = "test comment coming though [cool link](really cool)";
        String exampleComment2 = "test comment coming though [cool weird link ))](really cool)";
        String exampleComment3 = "test comment coming though [cool broken link(really cool)";

        String exampleCommentParsedCheck1 = "test comment coming though <a href=\"really cool\">cool link</a>";
        String exampleCommentParsedCheck2 = "test comment coming though <a href=\"really cool\">cool weird link ))</a>";
        String exampleCommentParsedCheck3 = "test comment coming though [cool broken link(really cool)";

        assertEquals (v.parseLinks(exampleComment1), exampleCommentParsedCheck1);
        assertEquals (v.parseLinks(exampleComment2), exampleCommentParsedCheck2);
        assertEquals (v.parseLinks(exampleComment3), exampleCommentParsedCheck3);
    }

}