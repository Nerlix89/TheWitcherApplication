package com.example.thewitcherapplication;

public class Location {
    private String title;
    private String description;
    private String imageUrl;

    public Location() {
    }

    public Location(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

