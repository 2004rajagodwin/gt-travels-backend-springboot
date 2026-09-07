package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, String>, JpaSpecificationExecutor<Booking> {

    List<Booking> findByPassenger_EmailOrderByCreatedAtDesc(String email);

    Page<Booking> findByPassenger_EmailOrderByCreatedAtDesc(String email, Pageable pageable);

    Page<Booking> findByBus_Operator_IdOrderByCreatedAtDesc(Long operatorId, Pageable pageable);

    List<Booking> findByBus_Operator_IdOrderByCreatedAtDesc(Long operatorId);

    Page<Booking> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Optional<Booking> findByRazorpayOrderId(String razorpayOrderId);

    Optional<Booking> findByRazorpayPaymentId(String razorpayPaymentId);

    @Query("""
            SELECT b
            FROM Booking b
            JOIN b.seats s
            WHERE s = :seat
            AND b.status = :status
            """)
    Optional<Booking> findBySeatAndStatus(
            @Param("seat") Seat seat,
            @Param("status") BookingStatus status);

    List<Booking> findBySchedule_Id(Long scheduleId);

    List<Booking> findByStatus(BookingStatus status);

    boolean existsByBookingId(String bookingId);

    boolean existsByPnr(String pnr);

    boolean existsByBookingReference(String bookingReference);

    Optional<Booking> findByPnr(String pnr);

    Optional<Booking> findByBookingReference(String bookingReference);

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.status = com.godwintech.gttravels.enums.BookingStatus.CONFIRMED
            """)
    Long getConfirmedBookings();

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.status = com.godwintech.gttravels.enums.BookingStatus.CONFIRMED
            """)
    Double getTotalRevenue();

    @Query("""
            SELECT COUNT(b)
            FROM Booking b
            WHERE b.bus.operator.id = :operatorId
            AND b.status = com.godwintech.gttravels.enums.BookingStatus.CONFIRMED
            """)
    Long countConfirmedByOperator(@Param("operatorId") Long operatorId);

    @Query("""
            SELECT COALESCE(SUM(b.totalAmount), 0)
            FROM Booking b
            WHERE b.bus.operator.id = :operatorId
            AND b.status = com.godwintech.gttravels.enums.BookingStatus.CONFIRMED
            """)
    Double getRevenueByOperator(@Param("operatorId") Long operatorId);

    @Query("""
            SELECT b FROM Booking b
            WHERE b.status = :status
            AND b.bookingTime < :expiredBefore
            """)
    List<Booking> findExpiredPendingBookings(
            @Param("status") BookingStatus status,
            @Param("expiredBefore") LocalDateTime expiredBefore);

    @Query("""
            SELECT DISTINCT s.id
            FROM Booking b
            JOIN b.seats s
            WHERE b.schedule.id = :scheduleId
            AND b.status IN :statuses
            """)
    List<Long> findOccupiedSeatIdsBySchedule(
            @Param("scheduleId") Long scheduleId,
            @Param("statuses") List<BookingStatus> statuses);

    @Query("""
            SELECT b
            FROM Booking b
            JOIN b.seats s
            WHERE b.schedule.id = :scheduleId
            AND s.id = :seatId
            AND b.status IN :statuses
            """)
    List<Booking> findActiveBookingsForSeatOnSchedule(
            @Param("scheduleId") Long scheduleId,
            @Param("seatId") Long seatId,
            @Param("statuses") List<BookingStatus> statuses);
}
