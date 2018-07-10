package com.lukepeckett.coopershooked;

public class StoryItem {

    private int imageResource;
    private int storyID;
    private String cardTitle;
    private String cardDescription;

    public StoryItem(int imageResource, String title, String description, int storyID) {
        this.imageResource = imageResource;
        this.cardTitle = title;
        this.cardDescription = description;
        this.storyID = storyID;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public int getStoryID() {
        return storyID;
    }
}
