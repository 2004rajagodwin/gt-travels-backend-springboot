package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // ==========================
    // Get All Routes
    // ==========================
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // ==========================
    // Get Route By Id
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<Route> getRoute(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    // ==========================
    // Source Dropdown
    // ==========================
    @GetMapping("/sources")
    public ResponseEntity<List<String>> getSources() {
        return ResponseEntity.ok(routeService.getAllSources());
    }

    // ==========================
    // Destination Dropdown
    // ==========================
    @GetMapping("/destinations")
    public ResponseEntity<List<String>> getDestinations() {
        return ResponseEntity.ok(routeService.getAllDestinations());
    }

}