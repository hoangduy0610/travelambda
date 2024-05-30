package com.lambda.travel.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Tour {

    public ArrayList<Activity> activities;
    public Hotel hotel;

    public ArrayList<Food> food;
    public String name;
    public String banner;
    public long pricing;

    public String destination_id;
    public Date arrival_date;
    public Date departure_date;
    public ArrayList<TourSchedule> tour_schedule;


    Tour(){

    }

    Tour(
        ArrayList<Activity> activities,
        Hotel hotel,
        ArrayList<Food> food,
        String name,
        long pricing,
        String destination_id,
        Date arrival_date,
        Date departure_date,
        String banner,
        ArrayList<TourSchedule> tour_schedule
    ) {
        this.activities = activities;
        this.food = food;
        this.hotel = hotel;
        this.name = name;
        this.pricing = pricing;
        this.destination_id = destination_id;
        this.arrival_date = arrival_date;
        this.departure_date = departure_date;
        this.banner = banner;
        this.tour_schedule = tour_schedule;
    }
}
