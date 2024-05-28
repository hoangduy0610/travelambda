package com.lambda.travel.model;

public class Review {
    public String remark;
    public int review;
    public String tour_id;
    public String user_id;

    public Review() {
    }

    public Review(String remark, int review, String tour_id, String user_id) {
        this.remark = remark;
        this.review = review;
        this.tour_id = tour_id;
        this.user_id = user_id;
    }
}
