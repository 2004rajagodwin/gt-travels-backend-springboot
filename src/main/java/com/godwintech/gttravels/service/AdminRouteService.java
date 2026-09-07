package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.RouteRequest;
import com.godwintech.gttravels.dto.RouteResponse;
import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminRouteService {

    private final RouteRepository routeRepository;

    public AdminRouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<RouteResponse> getAllRoutes() {

        return routeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RouteResponse getRoute(Long id) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        return mapToResponse(route);
    }

    public RouteResponse createRoute(RouteRequest request) {

        Route route = new Route();

        route.setSource(request.getSource());
        route.setDestination(request.getDestination());
        route.setDistance(request.getDistance());
        route.setDuration(request.getDuration());

        return mapToResponse(routeRepository.save(route));
    }

    public RouteResponse updateRoute(Long id,
                                     RouteRequest request) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        route.setSource(request.getSource());
        route.setDestination(request.getDestination());
        route.setDistance(request.getDistance());
        route.setDuration(request.getDuration());

        return mapToResponse(routeRepository.save(route));
    }

    public void deleteRoute(Long id) {

        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        routeRepository.delete(route);
    }

    private RouteResponse mapToResponse(Route route) {

        return new RouteResponse(

                route.getId(),

                route.getSource(),

                route.getDestination(),

                route.getDistance(),

                route.getDuration()

        );
    }
}