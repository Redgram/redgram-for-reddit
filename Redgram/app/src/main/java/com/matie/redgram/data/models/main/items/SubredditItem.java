package com.matie.redgram.data.models.main.items;

import com.matie.redgram.data.models.main.reddit.RedditObject;

/**
 * Created by matie on 2015-10-25.
 */
public class SubredditItem extends RedditObject{
    String name;
    private int accountsActive;
    private String description;
    private long subscribersCount;
    private String url;
    private boolean age;
    private String headerImage;
    private String headerTitle;
    private boolean publicTraffic;
    private String subredditType;
    private String submissionType;
    private String descriptionHtml;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccountsActive(int accountActive) {
        this.accountsActive = accountActive;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubscribersCount(long subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setAge(boolean age) {
        this.age = age;
    }

    public void setHeaderImage(String headerImage) {
        this.headerImage = headerImage;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public void setPublicTraffic(boolean publicTraffic) {
        this.publicTraffic = publicTraffic;
    }

    public void setSubredditType(String subredditType) {
        this.subredditType = subredditType;
    }

    public void setSubmissionType(String submissionType) {
        this.submissionType = submissionType;
    }

    public int getAccountsActive() {
        return accountsActive;
    }

    public String getDescription() {
        return description;
    }

    public long getSubscribersCount() {
        return subscribersCount;
    }

    public String getUrl() {
        return url;
    }

    public boolean isAge() {
        return age;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public boolean isPublicTraffic() {
        return publicTraffic;
    }

    public String getSubredditType() {
        return subredditType;
    }

    public String getSubmissionType() {
        return submissionType;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }
}
