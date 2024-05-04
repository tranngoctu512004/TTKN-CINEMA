package com.example.ttkncinema.activity.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Movie implements Parcelable {
    String Image;
    String Title;

    String Time;
    String Content;
    String Censorship;
    String Director;
    String date1;

    String date1_time1;
    String date1_time2;

    String date2;
    String date2_time1;
    String date3;
    String date3_time1;
    String date4;
    String date4_time1;
    String date5;
    String date5_time1;
    String id;
    String room1;

    public Movie(String image, String title, String time, String content, String censorship, String director, String date1, String date1_time1, String date1_time2, String date2, String date2_time1, String date3, String date3_time1, String date4, String date4_time1, String date5, String date5_time1, String id, String room1) {

        Image = image;
        Title = title;
        Time = time;
        Content = content;
        Censorship = censorship;
        Director = director;
        this.date1 = date1;
        this.date1_time1 = date1_time1;
        this.date1_time2 = date1_time2;
        this.date2 = date2;
        this.date2_time1 = date2_time1;
        this.date3 = date3;
        this.date3_time1 = date3_time1;
        this.date4 = date4;
        this.date4_time1 = date4_time1;
        this.date5 = date5;
        this.date5_time1 = date5_time1;
        this.id = id;
        this.room1 = room1;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setCensorship(String censorship) {
        Censorship = censorship;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public void setDate1_time1(String date1_time1) {
        this.date1_time1 = date1_time1;
    }

    public void setDate1_time2(String date1_time2) {
        this.date1_time2 = date1_time2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public void setDate2_time1(String date2_time1) {
        this.date2_time1 = date2_time1;
    }

    public void setDate3(String date3) {
        this.date3 = date3;
    }

    public void setDate3_time1(String date3_time1) {
        this.date3_time1 = date3_time1;
    }

    public void setDate4(String date4) {
        this.date4 = date4;
    }

    public void setDate4_time1(String date4_time1) {
        this.date4_time1 = date4_time1;
    }

    public void setDate5(String date5) {
        this.date5 = date5;
    }

    public void setDate5_time1(String date5_time1) {
        this.date5_time1 = date5_time1;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoom1(String room1) {
        this.room1 = room1;
    }

    public String getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public String getTime() {
        return Time;
    }

    public String getContent() {
        return Content;
    }

    public String getCensorship() {
        return Censorship;
    }

    public String getDirector() {
        return Director;
    }

    public String getDate1() {
        return date1;
    }

    public String getDate1_time1() {
        return date1_time1;
    }

    public String getDate1_time2() {
        return date1_time2;
    }

    public String getDate2() {
        return date2;
    }

    public String getDate2_time1() {
        return date2_time1;
    }

    public String getDate3() {
        return date3;
    }

    public String getDate3_time1() {
        return date3_time1;
    }

    public String getDate4() {
        return date4;
    }

    public String getDate4_time1() {
        return date4_time1;
    }

    public String getDate5() {
        return date5;
    }

    public String getDate5_time1() {
        return date5_time1;
    }

    public String getId() {
        return id;
    }

    public String getRoom1() {
        return room1;
    }

    public Movie() {
    }
    public List<String> getDates(){
        return Arrays.asList(date1,date2,date3,date4,date5);
    }
    public List<String> getTimes(String selectedDate) {
        // Lọc danh sách thời gian theo ngày được chọn
        List<String> times = new ArrayList<>();

        switch (selectedDate) {
            case "date1":
                times.add(date1_time1);
                times.add(date1_time2);
                break;
            case "date2":
                times.add(date2_time1);
                break;
            case "date3":
                times.add(date3_time1);
                break;
            case "date4":
                times.add(date4_time1);
                break;
            case "date5":
                times.add(date5_time1);
                break;
            default:
                break;
        }

        return times;
    }

    protected Movie(Parcel in) {
        // Đọc dữ liệu từ Parcel và khởi tạo các trường
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Ghi dữ liệu vào Parcel
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
