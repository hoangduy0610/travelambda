package com.lambda.travel.model;

public class Activity {
    public String detail;
    public String image;

    public String location;

    public String name;

    Activity() {

    }

    Activity(String detail, String image, String location, String name) {
        this.detail = detail;
        this.image = image;
        this.location = location;
        this.name = name;
    }

}
