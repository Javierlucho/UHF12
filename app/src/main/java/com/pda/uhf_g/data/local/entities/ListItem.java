package com.pda.uhf_g.data.local.entities;

public class ListItem {
    private int imageResId;
    private String title;

    private int position;

    private boolean isSelected;

    public ListItem(int imageResId, String title, int position) {
        this.setImageResId(imageResId);
        this.setTitle(title);
        this.setSelected(false);
        this.setPosition(position);
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
