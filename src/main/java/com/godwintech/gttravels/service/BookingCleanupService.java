package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.BookingProperties;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.PaymentStatus;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingCleanupService {

    private static final Logger log = LoggerFactory.getLogger(BookingCleanupService.class);
    private static final List<BookingStatus> OPEN_STATUSES =
            List.of(BookingStatus.PENDING, BookingStatus.PAYMENT_PENDING);

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final BookingProperties bookingProperties;

    public BookingCleanupService(
            SeatRepository seatRepository,
            BookingRepository bookingRepository,
            BookingProperties bookingProperties) {

        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.bookingProperties = bookingProperties;
    }

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void cleanupExpiredLocksAndBookings() {
        releaseExpiredSeatLocks();
        expireOpenBookings();
    }

    private void releaseExpiredSeatLocks() {
        LocalDateTime expiredBefore = LocalDateTime.now()
                .minusMinutes(bookingProperties.getSeatLockMinutes());

        List<Seat> expiredSeats = seatRepository.findExpiredLockedSeats(
                SeatStatus.LOCKED, expiredBefore);

        if (expiredSeats.isEmpty()) {
            return;
        }

        Set<Long> seatIds = new HashSet<>();
        for (Seat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedBy(null);
            seat.setLockedAt(null);
            seatIds.add(seat.getId());
        }

        seatRepository.saveAll(expiredSeats);
        log.info("Released {} expired seat lock(s)", expiredSeats.size());

        for (BookingStatus status : OPEN_STATUSES) {
            List<Booking> openBookings = bookingRepository.findByStatus(status);
            for (Booking booking : openBookings) {
                boolean hasExpiredSeat = booking.getSeats().stream()
                        .anyMatch(seat -> seatIds.contains(seat.getId()));
                if (hasExpiredSeat) {
                    markFailed(booking);
                }
            }
        }
    }

    private void expireOpenBookings() {
        LocalDateTime expiredBefore = LocalDateTime.now()
                .minusMinutes(bookingProperties.getPendingExpiryMinutes());

        for (BookingStatus status : OPEN_STATUSES) {
            List<Booking> expiredBookings = bookingRepository.findExpiredPendingBookings(
                    status, expiredBefore);
            for (Booking booking : expiredBookings) {
                markFailed(booking);
                log.info("Expired booking {}", booking.getBookingReference());
            }
        }
    }

    private void markFailed(Booking booking) {
        booking.setStatus(BookingStatus.FAILED);
        booking.setPaymentStatus(PaymentStatus.FAILED);
        for (Seat seat : booking.getSeats()) {
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedBy(null);
                seat.setLockedAt(null);
            }
        }
        seatRepository.saveAll(booking.getSeats());
        bookingRepository.save(booking);
    }
}
