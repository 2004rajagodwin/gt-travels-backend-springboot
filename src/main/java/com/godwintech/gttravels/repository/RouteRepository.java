package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findBySourceAndDestination(String source, String destination);

    // All unique source cities
    @Query("SELECT DISTINCT r.source FROM Route r ORDER BY r.source")
    List<String> findAllSources();

    // All unique destination cities
    @Query("SELECT DISTINCT r.destination FROM Route r ORDER BY r.destination")
    List<String> findAllDestinations();

}