package com.lambda.travel.model;

public class Activities {
    public String detail;
    public String image;

    public String location;

    public String name;

    Activities() {

    }

    Activities(String detail, String image, String location, String name) {
        this.detail = detail;
        this.image = image;
        this.location = location;
        this.name = name;
    }

}
