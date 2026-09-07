package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.entity.Coupon;
import com.godwintech.gttravels.entity.PromotionalOffer;
import com.godwintech.gttravels.enums.DiscountType;

public class FareDiscountContext {

    private Coupon coupon;
    private double couponDiscount;
    private PromotionalOffer offer;
    private double offerDiscount;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public PromotionalOffer getOffer() {
        return offer;
    }

    public void setOffer(PromotionalOffer offer) {
        this.offer = offer;
    }

    public double getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(double offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public String getCouponCode() {
        return coupon != null ? coupon.getCode() : null;
    }

    public DiscountType getCouponDiscountType() {
        return coupon != null ? coupon.getDiscountType() : null;
    }
}
