package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.OperatorBookingResponse;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorService {

    private final BookingRepository bookingRepository;
    private final UserService userService;

    public OperatorService(BookingRepository bookingRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
    }

    public List<OperatorBookingResponse> getBookings() {

        User operator = userService.getCurrentUser();

        return bookingRepository
                .findByBus_Operator_IdOrderByCreatedAtDesc(operator.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private OperatorBookingResponse toResponse(Booking booking) {
        return new OperatorBookingResponse(
                booking.getBookingId(),
                booking.getPassenger().getName(),
                booking.getPassenger().getPhone(),
                booking.getBus().getBusNumber(),
                booking.getSchedule().getRoute().getSource(),
                booking.getSchedule().getRoute().getDestination(),
                booking.getSeats().stream().map(Seat::getSeatNumber).toList(),
                booking.getTotalAmount(),
                booking.getStatus().name(),
                booking.getBookingTime()
        );
    }
}
