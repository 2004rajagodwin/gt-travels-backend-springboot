package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByBusId(Long busId);

    List<Seat> findByBusIdAndStatus(Long busId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id IN :ids")
    List<Seat> findAllByIdForUpdate(@Param("ids") List<Long> ids);

    @Query("""
            SELECT s FROM Seat s
            WHERE s.status = :status
            AND s.lockedAt IS NOT NULL
            AND s.lockedAt < :expiredBefore
            """)
    List<Seat> findExpiredLockedSeats(
            @Param("status") SeatStatus status,
            @Param("expiredBefore") LocalDateTime expiredBefore);
}
