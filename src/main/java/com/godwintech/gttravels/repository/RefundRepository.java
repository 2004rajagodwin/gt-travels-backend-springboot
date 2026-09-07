package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Refund;
import com.godwintech.gttravels.enums.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RefundRepository extends JpaRepository<Refund, Long>, JpaSpecificationExecutor<Refund> {

    Optional<Refund> findByBooking_BookingId(String bookingId);

    List<Refund> findByBooking_BookingIdOrderByCreatedAtDesc(String bookingId);

    Optional<Refund> findByRefundReference(String refundReference);

    boolean existsByRefundReference(String refundReference);

    boolean existsByBooking_BookingIdAndStatusIn(String bookingId, List<RefundStatus> statuses);
}
