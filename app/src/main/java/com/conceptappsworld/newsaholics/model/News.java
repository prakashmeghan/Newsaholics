package com.conceptappsworld.newsaholics.model;

/**
 * Created by Sprim on 09-10-2017.
 */

public class News {

    private String title;
    private String publicationDate;
    private String sectionName;

    public News(String _title, String _publicationDate, String _sectionName){
        title = _title;
        publicationDate = _publicationDate;
        sectionName = _sectionName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
