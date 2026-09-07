package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.DroppingPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DroppingPointRepository
        extends JpaRepository<DroppingPoint, Long> {

    // Get Dropping Points by Route
    List<DroppingPoint> findByRoute_IdOrderBySequenceNoAsc(Long routeId);

    List<DroppingPoint> findByRoute_IdAndActiveTrueOrderBySequenceNoAsc(Long routeId);

    // Get Dropping Points on routes actually served by this operator's buses
    @Query("select distinct dp from DroppingPoint dp " +
            "where dp.route.id in (select s.route.id from Schedule s where s.bus.operator.id = :operatorId)")
    List<DroppingPoint> findByOperatorId(@Param("operatorId") Long operatorId);
}