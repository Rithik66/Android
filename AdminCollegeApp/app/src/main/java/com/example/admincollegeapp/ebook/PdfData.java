package com.example.admincollegeapp.ebook;

public class PdfData {
    String url;
    String date;
    String time;
    String fileName,key;

    PdfData(){}

    public PdfData(String url, String date, String time ,String fileName,String key) {
        this.url = url;
        this.date = date;
        this.time = time;
        this.fileName = fileName;
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
