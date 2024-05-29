package com.lambda.travel.model;

public class BookingHistory {
    public String tourId;
    public Tour tourInfo;
    public TourBooking bookingInfo;
    public Location location;
    public Review review;

    public BookingHistory() {}

    public BookingHistory(String tourId, Tour t, TourBooking b, Location location, Review review) {
        this.tourId = tourId;
        this.tourInfo = t;
        this.bookingInfo = b;
        this.location = location;
        this.review = review;
    }
}
