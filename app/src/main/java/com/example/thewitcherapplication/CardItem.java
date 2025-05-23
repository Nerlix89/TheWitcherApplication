package com.example.thewitcherapplication;

import androidx.fragment.app.Fragment;

public class CardItem {
    public String id;
    public String title;
    public String imageUrl;

    public CardItem() {}

    public CardItem(String id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}


