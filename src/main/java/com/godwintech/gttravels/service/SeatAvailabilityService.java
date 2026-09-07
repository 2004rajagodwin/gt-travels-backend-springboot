package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SeatAvailabilityService {

    private static final List<BookingStatus> ACTIVE_BOOKING_STATUSES =
            List.of(BookingStatus.PENDING, BookingStatus.PAYMENT_PENDING, BookingStatus.CONFIRMED);

    private final BookingRepository bookingRepository;

    public SeatAvailabilityService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Set<Long> getOccupiedSeatIds(Long scheduleId) {
        return new HashSet<>(bookingRepository.findOccupiedSeatIdsBySchedule(
                scheduleId, ACTIVE_BOOKING_STATUSES));
    }

    public SeatStatus getEffectiveStatus(Seat seat, Long scheduleId, Set<Long> occupiedSeatIds) {
        if (!seat.isBookable()) {
            return SeatStatus.BLOCKED;
        }
        if (occupiedSeatIds.contains(seat.getId())) {
            return SeatStatus.BOOKED;
        }
        if (seat.getStatus() == SeatStatus.LOCKED) {
            List<com.godwintech.gttravels.entity.Booking> locks =
                    bookingRepository.findActiveBookingsForSeatOnSchedule(
                            scheduleId, seat.getId(),
                            List.of(BookingStatus.PENDING, BookingStatus.PAYMENT_PENDING));
            if (!locks.isEmpty()) {
                return SeatStatus.LOCKED;
            }
            return SeatStatus.AVAILABLE;
        }
        if (seat.getStatus() == SeatStatus.BLOCKED) {
            return SeatStatus.BLOCKED;
        }
        return SeatStatus.AVAILABLE;
    }

    public Map<Long, SeatStatus> getScheduleSeatStatusMap(Long scheduleId, List<Seat> seats) {
        Set<Long> occupied = getOccupiedSeatIds(scheduleId);
        Map<Long, SeatStatus> map = new HashMap<>();
        for (Seat seat : seats) {
            map.put(seat.getId(), getEffectiveStatus(seat, scheduleId, occupied));
        }
        return map;
    }

    public int countAvailableSeats(Long scheduleId, List<Seat> seats) {
        Set<Long> occupied = getOccupiedSeatIds(scheduleId);
        int count = 0;
        for (Seat seat : seats) {
            SeatStatus status = getEffectiveStatus(seat, scheduleId, occupied);
            if (status == SeatStatus.AVAILABLE) {
                count++;
            }
        }
        return count;
    }

    public boolean isSeatAvailableForSchedule(Seat seat, Long scheduleId) {
        return getEffectiveStatus(seat, scheduleId, getOccupiedSeatIds(scheduleId))
                == SeatStatus.AVAILABLE;
    }
}
