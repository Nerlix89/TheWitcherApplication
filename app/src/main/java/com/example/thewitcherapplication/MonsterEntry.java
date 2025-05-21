package com.example.thewitcherapplication;

public class MonsterEntry {
    public String title;
    public String quote;
    public String quoteAuthor;
    public String description;
    public String imageUrl;
    public String image01;
    public String image02;
    public String image03;
    public String image04;

    public MonsterEntry() {}

    public MonsterEntry(String title, String quote, String description, String quoteAuthor, String imageUrl,
                        String image01, String image02, String image03, String image04) {
        this.title = title;
        this.quote = quote;
        this.quoteAuthor = quoteAuthor;
        this.description = description;
        this.imageUrl = imageUrl;
        this.image01 = image01;
        this.image02 = image02;
        this.image03 = image03;
        this.image04 = image04;
    }
}

