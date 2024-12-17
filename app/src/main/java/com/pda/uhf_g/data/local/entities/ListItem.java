package com.pda.uhf_g.data.local.entities;

public class ListItem {
    private int imageResId;
    private String title;

    private int position;

    private String description;
    private String brand;
    private String serial;

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


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
