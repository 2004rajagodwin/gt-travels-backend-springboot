package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    List<RouteStop> findByRouteIdAndActiveTrueOrderBySequenceNoAsc(Long routeId);
}
