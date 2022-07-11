package com.example.admincollegeapp;

public class FacultyData {

    String name,email,post,specialization,image,department,key;

    FacultyData(){}

    public FacultyData(String name, String email, String post, String specialization, String image, String department,String key) {
        this.name = name;
        this.email = email;
        this.post = post;
        this.specialization = specialization;
        this.image = image;
        this.department = department;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
