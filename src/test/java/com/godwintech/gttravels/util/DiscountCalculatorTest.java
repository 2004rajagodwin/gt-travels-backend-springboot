package com.godwintech.gttravels.util;

import com.godwintech.gttravels.enums.DiscountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountCalculatorTest {

    @Test
    void percentage_respectsMaximumDiscount() {
        double discount = DiscountCalculator.calculate(DiscountType.PERCENTAGE, 50, 1000, 100.0);
        assertEquals(100.0, discount);
    }

    @Test
    void percentage_calculatesCorrectly() {
        double discount = DiscountCalculator.calculate(DiscountType.PERCENTAGE, 10, 800, null);
        assertEquals(80.0, discount);
    }

    @Test
    void fixed_capsAtSubtotal() {
        double discount = DiscountCalculator.calculate(DiscountType.FIXED, 500, 300, null);
        assertEquals(300.0, discount);
    }

    @Test
    void fixed_appliesFullAmount() {
        double discount = DiscountCalculator.calculate(DiscountType.FIXED, 100, 1000, null);
        assertEquals(100.0, discount);
    }
}
