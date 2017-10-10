package com.conceptappsworld.newsaholics.model;

public class News {

    private String title;
    private String publicationDate;
    private String sectionName;
    private String webUrl;

    public News(String _title, String _publicationDate, String _sectionName, String _weburl) {
        title = _title;
        publicationDate = _publicationDate;
        sectionName = _sectionName;
        webUrl = _weburl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
