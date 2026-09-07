package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBooking_BookingId(String bookingId);
}