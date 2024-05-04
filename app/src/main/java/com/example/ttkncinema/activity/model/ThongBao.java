package com.example.ttkncinema.activity.model;

public class ThongBao {
    String image;
    String text;
    String chitiettext;
    String time;
    String date;

    public ThongBao(String image, String text, String chitiettext) {
        this.image = image;
        this.text = text;
        this.chitiettext = chitiettext;
        this.time = time;
        this.date = date;
    }

    public ThongBao() {
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

    public String getChitiettext() {
        return chitiettext;
    }

    public void setChitiettext(String chitiettext) {
        this.chitiettext = chitiettext;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
