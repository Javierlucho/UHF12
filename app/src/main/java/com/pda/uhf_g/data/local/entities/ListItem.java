package com.pda.uhf_g.data.local.entities;

public class ListItem {
    private int imageResId;
    private String title;

    public ListItem(int imageResId, String title) {
        this.setImageResId(imageResId);
        this.setTitle(title);
    }


    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
