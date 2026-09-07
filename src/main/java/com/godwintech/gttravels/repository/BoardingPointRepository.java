package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.BoardingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardingPointRepository
        extends JpaRepository<BoardingPoint, Long> {

    // Get Boarding Points by Route
    List<BoardingPoint> findByRoute_IdOrderBySequenceNoAsc(Long routeId);

    List<BoardingPoint> findByRoute_IdAndActiveTrueOrderBySequenceNoAsc(Long routeId);

    // Get Boarding Points on routes actually served by this operator's buses
    @Query("select distinct bp from BoardingPoint bp " +
            "where bp.route.id in (select s.route.id from Schedule s where s.bus.operator.id = :operatorId)")
    List<BoardingPoint> findByOperatorId(@Param("operatorId") Long operatorId);
}