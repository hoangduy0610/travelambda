package com.lambda.travel.model;

import java.util.ArrayList;

public class Hotel {
    public String detail;

    public ArrayList<String> images;

    public String name;

    public Float star;

    Hotel() {

    }

    Hotel(String detail, ArrayList<String> images, String name, Float star){
        this.detail = detail;
        this.images = images;
        this.name = name;
        this.star = star;
    }
}
