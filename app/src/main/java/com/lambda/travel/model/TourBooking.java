package com.lambda.travel.model;

public class TourBooking {
    public String emailAddress;
    public String name;
    public String phone;
    public String tour_id;
    public String user_id;
    public BookingAmount amount;

    public TourBooking() {}

    public TourBooking(BookingAmount amount, String emailAddress, String name, String phone, String tour_id, String user_id) {
        this.amount = amount;
        this.emailAddress = emailAddress;
        this.name = name;
        this.phone = phone;
        this.tour_id = tour_id;
        this.user_id = user_id;
    }
}
