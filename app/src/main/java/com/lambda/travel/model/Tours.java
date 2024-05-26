package com.lambda.travel.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Tours {

    public ArrayList<Activities> activities;
    public Hotel hotel;

    public ArrayList<Food> food;


    Tours(){

    }

    Tours(ArrayList<Activities> activities, Hotel hotel, ArrayList<Food> food) {
        this.activities = activities;
        this.food = food;
        this.hotel = hotel;
    }
}
