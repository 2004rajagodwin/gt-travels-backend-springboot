package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.RouteRequest;
import com.godwintech.gttravels.dto.RouteResponse;
import com.godwintech.gttravels.service.AdminRouteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/routes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRouteController {

    private final AdminRouteService adminRouteService;

    public AdminRouteController(AdminRouteService adminRouteService) {
        this.adminRouteService = adminRouteService;
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {

        return ResponseEntity.ok(
                adminRouteService.getAllRoutes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRoute(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminRouteService.getRoute(id));
    }

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request) {

        return ResponseEntity.ok(
                adminRouteService.createRoute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteRequest request) {

        return ResponseEntity.ok(
                adminRouteService.updateRoute(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoute(
            @PathVariable Long id) {

        adminRouteService.deleteRoute(id);

        return ResponseEntity.ok(
                Map.of("message", "Route deleted successfully"));
    }
}