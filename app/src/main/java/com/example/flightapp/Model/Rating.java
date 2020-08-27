package com.example.flightapp.Model;

public class Rating {

    public Rating(){}

    private String userPhone;
    private String airlineIdl;
    private String rateValue;
    private String comment;

    public Rating(String userPhone, String airlineIdl, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.airlineIdl = airlineIdl;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getAirlineIdl() {
        return airlineIdl;
    }

    public void setAirlineIdl(String airlineIdl) {
        this.airlineIdl = airlineIdl;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
