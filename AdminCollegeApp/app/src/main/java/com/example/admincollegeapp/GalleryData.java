package com.example.admincollegeapp;

public class GalleryData {
    String fileName,date,time,url,category,key;

    GalleryData(){}

    public GalleryData(String fileName, String date, String time, String url ,String category,String key) {
        this.fileName = fileName;
        this.date = date;
        this.time = time;
        this.url = url;
        this.category=category;
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
