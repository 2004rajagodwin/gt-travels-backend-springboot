package com.godwintech.gttravels.dto;

public class BookingResponse {

    private String bookingId;
    private String bookingReference;
    private String razorpayOrderId;
    private Double amount;
    private String key;
    private FareBreakdownResponse fare;

    public BookingResponse() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FareBreakdownResponse getFare() {
        return fare;
    }

    public void setFare(FareBreakdownResponse fare) {
        this.fare = fare;
    }
}
