package com.godwintech.gttravels.util;

import com.godwintech.gttravels.enums.DiscountType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DiscountCalculator {

    private DiscountCalculator() {
    }

    public static double calculate(
            DiscountType type,
            double discountValue,
            double subtotal,
            Double maximumDiscount) {

        if (subtotal <= 0 || discountValue <= 0) {
            return 0;
        }

        double discount = switch (type) {
            case PERCENTAGE -> subtotal * discountValue / 100.0;
            case FIXED -> discountValue;
        };

        if (maximumDiscount != null && maximumDiscount > 0) {
            discount = Math.min(discount, maximumDiscount);
        }

        discount = Math.min(discount, subtotal);
        return round(discount);
    }

    public static double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
