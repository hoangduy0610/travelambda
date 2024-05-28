package com.lambda.travel.model;

public class BookingHistory {
    public Tour tourInfo;
    public TourBooking bookingInfo;
    public Location location;

    public BookingHistory() {}

    public BookingHistory(Tour t, TourBooking b, Location location) {
        this.tourInfo = t;
        this.bookingInfo = b;
        this.location = location;
    }
}
