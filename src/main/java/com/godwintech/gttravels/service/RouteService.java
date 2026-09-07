package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    // All Routes
    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    // Route By Id
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found"));
    }

    // Create Route
    public Route createRoute(Route route) {
        return routeRepository.save(route);
    }

    // Update Route
    public Route updateRoute(Long id, Route route) {

        Route existing = getRouteById(id);

        existing.setSource(route.getSource());
        existing.setDestination(route.getDestination());
        existing.setDistance(route.getDistance());
        existing.setDuration(route.getDuration());

        return routeRepository.save(existing);
    }

    // Delete Route
    public void deleteRoute(Long id) {

        Route route = getRouteById(id);

        routeRepository.delete(route);
    }

    // Dropdown Source Cities
    public List<String> getAllSources() {
        return routeRepository.findAllSources();
    }

    // Dropdown Destination Cities
    public List<String> getAllDestinations() {
        return routeRepository.findAllDestinations();
    }

}