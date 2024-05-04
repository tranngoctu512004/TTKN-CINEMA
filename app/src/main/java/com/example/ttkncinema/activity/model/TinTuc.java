package com.example.ttkncinema.activity.model;

public class TinTuc {
    String image;
    String text;
    String chitiettext;


    public TinTuc() {
    }

    public TinTuc(String image, String text, String chitiettext) {
        this.image = image;
        this.text = text;
        this.chitiettext = chitiettext;
    }

    public String getChitiettext() {
        return chitiettext;
    }

    public void setChitiettext(String chitiettext) {
        this.chitiettext = chitiettext;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
