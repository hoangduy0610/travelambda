package com.lambda.travel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Tour {

    public ArrayList<Activity> activities;
    public Hotel hotel;

    public ArrayList<Food> food;
    public String name;
    public long pricing;


    Tour(){

    }

    Tour(ArrayList<Activity> activities, Hotel hotel, ArrayList<Food> food, String name, long pricing) {
        this.activities = activities;
        this.food = food;
        this.hotel = hotel;
        this.name = name;
        this.pricing = pricing;
    }
}
