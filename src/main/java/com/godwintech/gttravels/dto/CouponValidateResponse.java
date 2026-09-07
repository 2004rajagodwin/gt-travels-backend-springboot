package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.DiscountType;

public class CouponValidateResponse {

    private String couponCode;
    private DiscountType discountType;
    private double discountAmount;
    private double subtotalBeforeDiscount;
    private double finalAmount;
    private FareBreakdownResponse fare;

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getSubtotalBeforeDiscount() { return subtotalBeforeDiscount; }
    public void setSubtotalBeforeDiscount(double subtotalBeforeDiscount) { this.subtotalBeforeDiscount = subtotalBeforeDiscount; }
    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }
    public FareBreakdownResponse getFare() { return fare; }
    public void setFare(FareBreakdownResponse fare) { this.fare = fare; }
}
