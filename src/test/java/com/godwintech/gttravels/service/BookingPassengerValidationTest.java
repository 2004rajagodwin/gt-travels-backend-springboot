package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.CreateBookingRequest;
import com.godwintech.gttravels.dto.PassengerRequest;
import com.godwintech.gttravels.enums.PassengerGender;
import com.godwintech.gttravels.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingPassengerValidationTest {

    @Test
    void validatePassengers_rejectsMismatchedSeatCount() throws Exception {
        BookingService service = new BookingService(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        CreateBookingRequest request = new CreateBookingRequest();
        request.setSeatIds(List.of(1L, 2L, 3L));
        request.setPassengers(List.of(passenger(1L), passenger(2L)));

        Method method = BookingService.class.getDeclaredMethod("validatePassengers", CreateBookingRequest.class);
        method.setAccessible(true);

        assertThrows(BadRequestException.class, () -> {
            try {
                method.invoke(service, request);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void validatePassengers_rejectsInvalidSeatAssignment() throws Exception {
        BookingService service = new BookingService(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

        CreateBookingRequest request = new CreateBookingRequest();
        request.setSeatIds(List.of(1L));
        PassengerRequest passenger = passenger(99L);
        request.setPassengers(List.of(passenger));

        Method method = BookingService.class.getDeclaredMethod("validatePassengers", CreateBookingRequest.class);
        method.setAccessible(true);

        assertThrows(BadRequestException.class, () -> {
            try {
                method.invoke(service, request);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    private PassengerRequest passenger(Long seatId) {
        PassengerRequest p = new PassengerRequest();
        p.setSeatId(seatId);
        p.setName("Rahul Sharma");
        p.setAge(28);
        p.setGender(PassengerGender.MALE);
        p.setMobile("9876543210");
        p.setEmail("rahul@example.com");
        return p;
    }
}
