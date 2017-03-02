package com.matie.redgram.ui.common.utils.text.markdown;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.matie.redgram.ui.common.utils.text.CustomSpanListener;
import com.matie.redgram.ui.common.utils.text.spans.CodeSpan;
import com.matie.redgram.ui.common.utils.text.spans.CustomClickable;
import com.matie.redgram.ui.common.utils.text.spans.CustomSuperscriptSpan;
import com.matie.redgram.ui.common.utils.text.spans.LinkClickableSpan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matie on 2017-01-08.
 */
public class MDParser implements MDSpanListener {

    public static final String SPAN_URL = "SPAN_URL";
    //note EditText is a TextView
    private TextView view;
    private CharSequence stringToParse;
    private MDSpannableStringBuilder spannableStringBuilder;
    private CustomSpanListener spanListener;
    private MDStyle style;

    private boolean codeSpansApplied = false;
    private boolean linkSpansApplied = false;
    private Queue<Object> spanQueue = new LinkedList<>();

    public MDParser() {}

    public TextView getView() {
        return view;
    }

    public MDParser setView(TextView textView, String stringToParse) {
        view = textView;
        setText(stringToParse);

        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Layout layout = view.getLayout();
            if(layout != null && spanQueue.size() > 0){
                Object span = spanQueue.remove();
                if(span instanceof CustomSuperscriptSpan){
                    int lineNumber = layout.getLineForOffset(spannableStringBuilder.getSpanStart(span));
                    int lineStart = layout.getLineStart(lineNumber);
                    int lineEnd = layout.getLineEnd(lineNumber);

                    MDSpannableStringBuilder firstHalf = (MDSpannableStringBuilder) spannableStringBuilder.subSequence(0, lineStart);
                    MDSpannableStringBuilder secondHalf = (MDSpannableStringBuilder) spannableStringBuilder.subSequence(lineStart, spannableStringBuilder.length());
                    int nestedLevel = ((CustomSuperscriptSpan) span).getNestedLevel();

                    spannableStringBuilder = (MDSpannableStringBuilder) firstHalf
                                                .append(addCountNewLines(nestedLevel/2))
                                                .append(secondHalf);

                    setText(spannableStringBuilder);
                }
            }
        });

        return this;
    }

    private CharSequence addCountNewLines(int count) {
        CharSequence newLines = "";
        for(int i = 0; i <= count; i++){
            newLines = newLines + "\n";
        }
        return newLines;
    }

    private void setText(CharSequence stringToParse){
        if(stringToParse != null){
            this.stringToParse = stringToParse;
            this.spannableStringBuilder = new MDSpannableStringBuilder(stringToParse, this);
            setText(spannableStringBuilder);
        }
    }

    private void setText(MDSpannableStringBuilder spannableBuilder){
        view.setText(spannableBuilder, TextView.BufferType.SPANNABLE);
    }

    public MDSpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }

    public CharSequence getStringToParse() {
        return stringToParse;
    }

    public MDParser addSpanListener(CustomSpanListener listener) {
        this.spanListener = listener;
        return this;
    }

    protected void parse(){
        parseCode(0, spannableStringBuilder.length());
        parseCodeBlock(0, spannableStringBuilder.length());
        parseLink(0, spannableStringBuilder.length());
        parseSubUser(0, spannableStringBuilder.length());
        parseList(0, spannableStringBuilder.length());
        parseSeparator(0, spannableStringBuilder.length());
        parseSuperScript(0, spannableStringBuilder.length());
        parseBlockQuotes(0, spannableStringBuilder.length());
        parseBold(0, spannableStringBuilder.length());
        parseItalic(0, spannableStringBuilder.length());
        parseStrike(0, spannableStringBuilder.length());
    }

    public MDParser setStyle(MDStyle style){
        this.style = style;
        return this;
    }

    public MDStyle getStyle(){
        if(style == null){
            //default style
            style = new MDStyle(Color.rgb(128,128,128),
                    Color.rgb(0, 0, 0),
                    Color.rgb(6,69,173),
                    false,
                    Color.rgb(0, 0, 0),
                    Color.rgb(211,211,211));
        }
        return style;
    }

    protected MDParser parseCode(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.CODE.getPattern(), start, end);
            while (matcher.find()) {

                BackgroundColorSpan codeHighlighterSpan = new BackgroundColorSpan(getStyle().getCodeBackgroundColor());
                TypefaceSpan monospace = new TypefaceSpan("monospace");
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getStyle().getCodeTextColor());

                CodeSpan codeSpan = new CodeSpan(codeHighlighterSpan, foregroundColorSpan, monospace);

                setSpanAndReplaceText(codeSpan, matcher.start(), matcher.end(), matcher.group(2), true);
                reset(matcher);
            }
        }
        codeSpansApplied = true;
        return this;
    }
    private MDParser parseCodeBlock(int start, int end) {
        return this;
    }

    protected MDParser parseStrike(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.STRIKE.getPattern(), start, end);
            while (matcher.find()) {
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                setSpanAndReplaceText(strikethroughSpan, matcher.start(), matcher.end(), matcher.group(2), true);
                reset(matcher);
            }
        }
        return this;
    }

    protected MDParser parseItalic(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.ITALIC.getPattern(), start, end);
            while (matcher.find()) {
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StyleSpan styleSpan = new StyleSpan(Typeface.ITALIC);
                setSpanAndReplaceText(styleSpan, matcher.start(), matcher.end(), matcher.group(2), true);
                reset(matcher);
            }
        }
        return this;
    }

    protected MDParser parseBold(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.BOLD.getPattern(), start, end);
            while(matcher.find()){
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                setSpanAndReplaceText(styleSpan, matcher.start(), matcher.end(), matcher.group(2), true);
                reset(matcher);
            }
        }
        return this;
    }

    protected MDParser parseLink(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.LINK.getPattern(), start, end);
            while(matcher.find()){
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }
                //pass the actual string as data to the span
                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put(SPAN_URL, matcher.group(2));

                LinkClickableSpan customClickable;
                if(spanListener != null){
                    customClickable = new LinkClickableSpan(spanListener, getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }else{
                    customClickable = new LinkClickableSpan(getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }

                setSpanAndReplaceText(customClickable, matcher.start(), matcher.end(), matcher.group(1), true);
                reset(matcher);
            }
            linkSpansApplied = true;
            //parse full links after MD links are applied
            parseFullLinks(0, spannableStringBuilder.length());
        }

        return this;
    }

    protected MDParser parseFullLinks(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.FULL_LINK.getPattern(), start, end);
            while(matcher.find()){

                if((linkSpansApplied && isLinkSpanApplied(matcher)) || (codeSpansApplied && isCodeSpanApplied(matcher))){
                    continue;
                }

                HashMap<String, String> dataMap = new HashMap<>();
                dataMap.put(SPAN_URL, matcher.group()); //full match

                LinkClickableSpan customClickable;
                if(spanListener != null){
                    customClickable = new LinkClickableSpan(spanListener, getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }else{
                    customClickable = new LinkClickableSpan(getStyle().isUnderlineLink(), getStyle().getLinkColor(), dataMap);
                }

                setSpan(customClickable, matcher.start(), matcher.end());
            }
        }
        return this;
    }


    protected MDParser parseSubUser(int start, int end) {
        if(spannableStringBuilder != null) {
            Matcher matcher = getMatches(MDHighlights.SUB_USER.getPattern(), start, end);
            while (matcher.find()) {
                //if next match has a link span then skip to the next match
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }

                CustomClickable customClickable;
                if(spanListener != null){
                    customClickable = new CustomClickable(spanListener, true, getStyle().getLinkColor());
                }else{
                    customClickable = new CustomClickable(true, getStyle().getLinkColor());
                }

                setSpan(customClickable, matcher.start(), matcher.end());
            }
        }
        return this;
    }

    protected MDParser parseHeader(int start, int end) {
        if(spannableStringBuilder != null) {

        }
        return this;
    }

    protected MDParser parseSeparator(int start, int end) {
        if(spannableStringBuilder != null) {

        }
        return this;
    }

    protected MDParser parseList(int start, int end) {
        parseUnOrderedLists(start, end);
        parseOrderedLists(start, end);
        return this;
    }

    protected MDParser parseUnOrderedLists(int start, int end) {
        return this;
    }

    protected MDParser parseOrderedLists(int start, int end) {
        return this;
    }

    protected MDParser parseBlockQuotes(int start, int end) {
        return this;
    }

    /**
     * foo^bar
     * group 0 - ^bar
     * group 1 - bar
     *
     * foo^(bar)
     * group 0 - ^(bar)
     * group 1 - (bar)
     * group 2 - bar
     *
     * @param start
     * @param end
     * @return
     */
    protected MDParser parseSuperScript(int start, int end) {
        if(spannableStringBuilder != null){
            Matcher matcher = getMatches(MDHighlights.SUPERSCRIPT.getPattern(), start, end);

            Stack<MDMatcherWrapper> nestedMatches = new Stack<>();
            int parentEnd = -1;
            while(matcher.find()){
                if(codeSpansApplied && isCodeSpanApplied(matcher)){
                    continue;
                }

                List<String> groups = new ArrayList<>();
                for(int i = 0 ; i <= matcher.groupCount(); i++){
                    if(matcher.group(i) != null){
                        groups.add(matcher.group(i));
                    }
                }
                MDMatcherWrapper wrapper = new MDMatcherWrapper(matcher.start(), matcher.end(), groups);

                //super^super1^super2^super3
                if(parentEnd != -1){
                    if(wrapper.getStart() == parentEnd){
                        //if current match is child
                        nestedMatches.push(wrapper);
                        parentEnd = wrapper.getEnd();
                    }else{
                        //end of the nested chain
                        //apply the spans on the current nest
                        applyNestedSpans(nestedMatches);
                        //refresh nesting operation
                        nestedMatches.clear();
                        //reset everything
                        reset(matcher);
                        parentEnd = -1;
                    }
                }else{
                    //initial step
                    //o1
                    nestedMatches.push(wrapper);
                    parentEnd = wrapper.getEnd();
                }

            }
            //for any remaining nests
            if(!nestedMatches.isEmpty()){
                applyNestedSpans(nestedMatches);
            }

        }
        return this;
    }

    private void applyNestedSpans(Stack<MDMatcherWrapper> nestedMatches) {
        int originalSize = nestedMatches.size();
        while(!nestedMatches.isEmpty()){
            int size = nestedMatches.size();
            MDMatcherWrapper currentMatch = nestedMatches.pop();

            String targetGroup = "";
            //brackets ()
            if(currentMatch.getGroups().size() > 2){
                targetGroup = currentMatch.getGroups().get(2);
            }else{
                targetGroup = currentMatch.getGroups().get(1);
            }

            int start = currentMatch.getStart();
            int end = currentMatch.getEnd();

            CustomSuperscriptSpan span = new CustomSuperscriptSpan(size, originalSize - size);

            if(nestedMatches.size() == 0 && originalSize > 2){
                setSpanAndReplaceText(span, start, end, targetGroup, true);
            }else{
                setSpanAndReplaceText(span, start, end, targetGroup, false);
            }

        }

    }

    private boolean isLinkSpanApplied(Matcher matcher) {
        if(spannableStringBuilder != null) {
            LinkClickableSpan[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), LinkClickableSpan.class);
            for (LinkClickableSpan span : spans) {
                if (spannableStringBuilder.getSpanStart(span) <= matcher.start()
                        && spannableStringBuilder.getSpanEnd(span) >= matcher.end()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCodeSpanApplied(Matcher matcher) {
        if(spannableStringBuilder != null){
            CodeSpan[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), CodeSpan.class);
            for(CodeSpan span: spans){
                if (spannableStringBuilder.getSpanStart(span) <= matcher.start()
                        && spannableStringBuilder.getSpanEnd(span) >= matcher.end()) {
                    return true;
                }
            }
        }
        return false;
    }

//    private void clearSpansInCodeRange(Matcher matcher) {
//        if(spannableStringBuilder != null){
//            Object[] spans = spannableStringBuilder.getSpans(matcher.start(), matcher.end(), Object.class);
//            for(Object span: spans){
//                if(span instanceof StyleSpan
//                        && (spannableStringBuilder.getSpanStart(span) <= matcher.start()
//                            && spannableStringBuilder.getSpanEnd(span) >= matcher.end())){
//                    int spanStart = spannableStringBuilder.getSpanStart(span);
//                    int spanEnd = spannableStringBuilder.getSpanEnd(span);
//                    spannableStringBuilder.removeSpan(span);
//                    if(Typeface.BOLD == ((StyleSpan) span).getStyle()){
//                        setSpan(new StyleSpan(Typeface.BOLD), spanStart, matcher.start());
//                        setSpan(new StyleSpan(Typeface.BOLD), matcher.end(), spanEnd);
//                    }else if(Typeface.ITALIC == ((StyleSpan) span).getStyle()){
//                        setSpan(new StyleSpan(Typeface.ITALIC), spanStart, matcher.start());
//                        setSpan(new StyleSpan(Typeface.ITALIC), matcher.end(), spanEnd);
//                    }
//                }else if(!(span instanceof LinkClickableSpan)
//                            && !(span instanceof StrikethroughSpan
//                                    && spannableStringBuilder.getSpanStart(span) <= matcher.start()
//                                    && spannableStringBuilder.getSpanEnd(span) >= matcher.end())){
//                    spannableStringBuilder.removeSpan(span);
//                }
//            }
//        }
//    }

    private Matcher getMatches(final Pattern pattern, final int start, final int end){
        return pattern.matcher(spannableStringBuilder.subSequence(start, end));
    }

    private void setSpan(Object spanObject, int start, int end){
        setSpan(spanObject, start, end, true);
    }

    private void setSpan(Object spanObject, int start, int end, boolean triggerListener){
       spannableStringBuilder.setSpan(spanObject, start, end,
                            SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE, triggerListener);

    }

    private void setSpanAndReplaceText(Object what, int start, int end, String group, boolean triggerListener) {
        spannableStringBuilder = spannableStringBuilder
                    .setSpanAndReplaceText(what, start, end, group, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE, triggerListener);

    }

    private MDSpannableStringBuilder replaceWithSequence(int start, int end, CharSequence sequence) {
        return spannableStringBuilder.replace(start, end, sequence);
    }


    private void reset(Matcher matcher){
        matcher.reset(spannableStringBuilder);
    }

    public void build(){
        if(view != null && view.getText().length() > 0){
            parse();
            view.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
            view.setTextColor(getStyle().getTextColor());
        }
    }

    public CustomSpanListener getSpanListener() {
        return spanListener;
    }

    @Override
    public void onSpanAdded(Object span) {
        if(span instanceof CustomSuperscriptSpan){
            spanQueue.add(span);
            setText(spannableStringBuilder);
        }
    }
}
