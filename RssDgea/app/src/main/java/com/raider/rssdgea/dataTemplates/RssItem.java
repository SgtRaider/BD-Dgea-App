package com.raider.rssdgea.dataTemplates;

import java.util.Date;

/**
 * Created by Raider on 14/04/16.
 */
public class RssItem {

    private String title;
    private String firstCategory;
    private String description;
    private String url;
    private String guid;
    private String pubDate;
    private String author;

    public RssItem(String title, String guid, String author, String firstCategory, String description, String url, String pubDate) {
        this.title = title;
        this.firstCategory = firstCategory;;
        this.description = description;
        this.url = url;
        this.pubDate = pubDate;
        this.author = author;
        this.guid = guid;
    }

    public RssItem() {

    }

    public String getTitle() {
        return this.title;
    }

    public String getFirstCategory() {
        return this.firstCategory;
    }

    public String getDescription() {
        return this.description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFirstCategory(String firstCategory) {
        this.firstCategory = firstCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
