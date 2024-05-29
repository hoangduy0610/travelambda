package com.lambda.travel.model;

public class Location {
    public String city;
    public String country;
    public String description;
    public String image;
    public String province;

    public Location() {
    }

    public Location(String city, String country, String description, String image, String province) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.image = image;
        this.province = province;
    }
}
