package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.FareBreakdownResponse;
import com.godwintech.gttravels.entity.BoardingPoint;
import com.godwintech.gttravels.entity.Schedule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FareCalculationServiceTest {

    @Test
    void calculate_usesBackendValues_notFrontendInput() {
        FarePropertiesStub props = new FarePropertiesStub();
        FareCalculationService service = new FareCalculationService(props);

        Schedule schedule = new Schedule();
        schedule.setFare(800.0);

        BoardingPoint boardingPoint = new BoardingPoint();
        boardingPoint.setBoardingCharge(20.0);

        FareBreakdownResponse fare = service.calculate(schedule, 1, boardingPoint);

        assertEquals(800.0, fare.getBaseFare());
        assertEquals(20.0, fare.getBoardingCharges());
        assertEquals(30.0, fare.getConvenienceFee());
        assertEquals(42.5, fare.getTax());
        assertEquals(892.5, fare.getTotalAmount());
        assertNotEquals(100.0, fare.getTotalAmount());
    }

    @Test
    void calculate_scalesWithSeatCount() {
        FareCalculationService service = new FareCalculationService(new FarePropertiesStub());
        Schedule schedule = new Schedule();
        schedule.setFare(500.0);
        BoardingPoint boardingPoint = new BoardingPoint();
        boardingPoint.setBoardingCharge(10.0);

        FareBreakdownResponse fare = service.calculate(schedule, 3, boardingPoint);
        assertEquals(1500.0, fare.getBaseFare());
        assertEquals(3, fare.getSeatCount());
    }

    private static class FarePropertiesStub extends com.godwintech.gttravels.config.FareProperties {
        @Override
        public double getConvenienceFeePerSeat() {
            return 30;
        }

        @Override
        public double getTaxPercent() {
            return 5;
        }

        @Override
        public double getDefaultBoardingCharge() {
            return 20;
        }
    }
}
