package com.matie.redgram.data.models.main.items;

import android.net.Uri;

import com.matie.redgram.data.models.main.reddit.RedditObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matie on 09/05/15.
 */
public class PostItem extends RedditObject {

    public enum Type{
        DEFAULT,
        SELF,
        IMAGE,
        GIF,
        GFYCAT,
        YOUTUBE,
        IMGUR,
        IMGUR_GALLERY,
        IMGUR_ALBUM,
        IMGUR_CUSTOM_GALLERY,
//        IMGUR_TAG,
       // IMGUR_MEME, not sure of this
//        IMGUR_SUBREDDIT
    }

    private String id;
    private String name;
    private String likes;
    private Type type;
    private String author;
    private int time;
    private String url;
    private String permalink;
    private String thumbnail;
    private String title;
    private String domain;
    private String text;
    private String htmlText;
    private int numComments;
    private boolean isSelf;
    private int score;
    private boolean isAdult;
    private String distinguished;
    private String subreddit;
    private boolean isSaved;
    private boolean isHidden;

    public PostItem(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String distinguished() {
        return distinguished;
    }

    public void setIsDistinguished(String distinguished) {
        this.distinguished = distinguished;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getLikes() {
        return likes;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        this.isSaved = saved;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public Type identifyType() {
        if(isSelf){
            return Type.SELF;
        }

        if(isDirectMedia()){
            return Type.IMAGE;
        }

        if(isImgurContent()){
            if(isImgurDirectMedia() && !isAnimated()){
                return Type.IMGUR;
            }
            if(isImgurAlbum()){
                return Type.IMGUR_ALBUM;
            }
            if(isImgurGallery()){
                return Type.IMGUR_GALLERY;
            }
            if(isImgurCustomGallery()){
                return Type.IMGUR_CUSTOM_GALLERY;
            }
        }

        if(isAnimated()){
            return Type.GIF;
        }

        if(isGfyCat()){
            return Type.GFYCAT;
        }
        if(isYoutube()){
            return Type.YOUTUBE;
        }

        return Type.DEFAULT; //external link
    }

    private boolean isYoutube() {
        Uri uri = Uri.parse(url);
        if(uri.getHost() != null &&
                (uri.getHost().endsWith("youtube.com") || uri.getHost().endsWith("youtu.be")))
            return true;
        else
            return false;
    }

    private boolean isGfyCat() {
        Uri uri = Uri.parse(url);
        if(uri.getHost() != null && (uri.getHost().endsWith("gfycat.com") || getDomain().equalsIgnoreCase("gfycat.com")))
            return true;
        else
            return false;
    }

    private boolean isImgurAlbum() {
        String regex = "imgur.com/a/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isImgurDirectMedia() {
        String regex = "imgur.com/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isImgurTag() {
        String regex = "imgur.com/t/[a-zA-Z0-9-_]/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isImgurSubreddit() {
        String regex = "imgur.com/r/[a-zA-Z0-9-_]/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isImgurCustomGallery() {
        String regex = "imgur.com/g/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isImgurGallery() {
        String regex = "imgur.com/gallery/[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    // gif or mp4 only
    private boolean isAnimated() {
        if(url.endsWith(".gifv") || url.endsWith(".mp4")){
            return true;
        }else{
            return false;
        }
    }

    private boolean isDirectMedia() {
        if(url.endsWith(".png") || url.endsWith(".jpg"))
            return true;
        else
            return false;
    }

    public boolean isImgurContent() {
        Uri uri = Uri.parse(url);
        if(uri.getHost() != null && uri.getHost().endsWith("imgur.com"))
            return true;
        else
            return false;
    }

    @Override
    public boolean equals(Object o) {
        boolean flag = false;

        if(o instanceof PostItem){
            PostItem item = (PostItem)o;
            flag = this.id.equalsIgnoreCase(item.getId());
        }

        return flag;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

}
