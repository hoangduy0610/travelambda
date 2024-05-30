package com.lambda.travel.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TourSchedule {
    public Date date;
    public String detail;

    TourSchedule(){

    }

    TourSchedule(
        Date date,
        String detail
    ){
        this.date = date;
        this.detail = detail;
    }
}
