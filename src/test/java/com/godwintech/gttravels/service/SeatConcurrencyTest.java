package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.*;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.TripStatus;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.repository.BoardingPointRepository;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.DroppingPointRepository;
import com.godwintech.gttravels.repository.ScheduleRepository;
import com.godwintech.gttravels.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeatConcurrencyTest {

    @Mock private ScheduleRepository scheduleRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private BoardingPointRepository boardingPointRepository;
    @Mock private DroppingPointRepository droppingPointRepository;
    @Mock private BookingRepository bookingRepository;

    private BookingValidationService bookingValidationService;

    @BeforeEach
    void setUp() {
        SeatAvailabilityService seatAvailabilityService = new SeatAvailabilityService(bookingRepository);
        bookingValidationService = new BookingValidationService(
                scheduleRepository,
                seatRepository,
                boardingPointRepository,
                droppingPointRepository,
                seatAvailabilityService);
    }

    @Test
    void validate_rejectsSeatAlreadyTakenByAnotherUser() {
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setStatus(BusStatus.ACTIVE);

        Route route = new Route();
        route.setId(10L);

        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setBus(bus);
        schedule.setRoute(route);
        schedule.setActive(true);
        schedule.setTripStatus(TripStatus.SCHEDULED);
        schedule.setJourneyDate(LocalDate.now().plusDays(1));
        schedule.setDepartureTime(LocalDateTime.now().plusDays(1));

        Seat seat = new Seat();
        seat.setId(10L);
        seat.setSeatNumber("L1");
        seat.setBus(bus);
        seat.setBookable(true);

        BoardingPoint boarding = new BoardingPoint();
        boarding.setId(1L);
        boarding.setRoute(route);
        boarding.setActive(true);

        DroppingPoint dropping = new DroppingPoint();
        dropping.setId(2L);
        dropping.setRoute(route);
        dropping.setActive(true);

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
        when(seatRepository.findAllById(List.of(10L))).thenReturn(List.of(seat));
        when(bookingRepository.findOccupiedSeatIdsBySchedule(eq(1L), anyList()))
                .thenReturn(List.of(10L));

        assertThrows(ConflictException.class, () ->
                bookingValidationService.validate(1L, List.of(10L), 1L, 2L));
    }
}
