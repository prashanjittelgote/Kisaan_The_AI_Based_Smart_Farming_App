package com.mountreachsolution.kisaan.POPJO;

public class NewsModel {

    String title, description, imageUrl, link;

    public NewsModel(String title, String description, String imageUrl, String link) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getLink() { return link; }
}
