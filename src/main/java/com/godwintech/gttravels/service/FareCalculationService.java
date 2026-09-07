package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.FareProperties;
import com.godwintech.gttravels.dto.FareBreakdownResponse;
import com.godwintech.gttravels.dto.FareDiscountContext;
import com.godwintech.gttravels.entity.BoardingPoint;
import com.godwintech.gttravels.entity.Schedule;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class FareCalculationService {

    private final FareProperties fareProperties;

    public FareCalculationService(FareProperties fareProperties) {
        this.fareProperties = fareProperties;
    }

    public FareBreakdownResponse calculate(Schedule schedule, int seatCount, BoardingPoint boardingPoint) {
        return calculate(schedule, seatCount, boardingPoint, new FareDiscountContext());
    }

    public FareBreakdownResponse calculate(
            Schedule schedule,
            int seatCount,
            BoardingPoint boardingPoint,
            FareDiscountContext discountContext) {

        if (seatCount <= 0) {
            throw new IllegalArgumentException("At least one seat is required");
        }

        double baseFare = round((schedule.getFare() != null ? schedule.getFare() : 0) * seatCount);

        double perSeatBoarding = boardingPoint != null && boardingPoint.getBoardingCharge() != null
                ? boardingPoint.getBoardingCharge()
                : fareProperties.getDefaultBoardingCharge();
        double boardingCharges = round(perSeatBoarding * seatCount);
        double convenienceFee = round(fareProperties.getConvenienceFeePerSeat() * seatCount);

        double subtotal = baseFare + boardingCharges + convenienceFee;
        double couponDiscount = discountContext != null ? discountContext.getCouponDiscount() : 0;
        double offerDiscount = discountContext != null ? discountContext.getOfferDiscount() : 0;
        double discount = round(couponDiscount + offerDiscount);

        double taxable = Math.max(0, subtotal - discount);
        double tax = round(taxable * fareProperties.getTaxPercent() / 100.0);
        double totalAmount = round(taxable + tax);

        FareBreakdownResponse response = new FareBreakdownResponse();
        response.setBaseFare(baseFare);
        response.setBoardingCharges(boardingCharges);
        response.setConvenienceFee(convenienceFee);
        response.setTax(tax);
        response.setDiscount(discount);
        response.setCouponDiscount(couponDiscount);
        response.setOfferDiscount(offerDiscount);
        response.setTotalAmount(totalAmount);
        response.setSeatCount(seatCount);
        if (discountContext != null && discountContext.getCoupon() != null) {
            response.setCouponCode(discountContext.getCoupon().getCode());
        }
        return response;
    }

    public double calculateSubtotal(Schedule schedule, int seatCount, BoardingPoint boardingPoint) {
        double baseFare = (schedule.getFare() != null ? schedule.getFare() : 0) * seatCount;
        double perSeatBoarding = boardingPoint != null && boardingPoint.getBoardingCharge() != null
                ? boardingPoint.getBoardingCharge()
                : fareProperties.getDefaultBoardingCharge();
        return round(baseFare + perSeatBoarding * seatCount
                + fareProperties.getConvenienceFeePerSeat() * seatCount);
    }

    private double round(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
