package com.godwintech.gttravels.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentOrderRequest {

    @NotBlank
    private String bookingId;

    @NotBlank
    private String paymentId;

    @NotBlank
    private String orderId;

    @NotBlank
    private String signature;

    public PaymentOrderRequest() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}