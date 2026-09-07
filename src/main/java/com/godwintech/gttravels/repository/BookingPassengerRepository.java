package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.BookingPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingPassengerRepository extends JpaRepository<BookingPassenger, Long> {

    List<BookingPassenger> findByBooking_BookingId(String bookingId);
}
