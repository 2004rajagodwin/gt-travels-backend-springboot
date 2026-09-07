package com.godwintech.gttravels.dto;

public class FareBreakdownResponse {

    private double baseFare;
    private double boardingCharges;
    private double convenienceFee;
    private double tax;
    private double discount;
    private double couponDiscount;
    private double offerDiscount;
    private double totalAmount;
    private int seatCount;
    private String couponCode;

    public double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
    }

    public double getBoardingCharges() {
        return boardingCharges;
    }

    public void setBoardingCharges(double boardingCharges) {
        this.boardingCharges = boardingCharges;
    }

    public double getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(double convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public double getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(double offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
