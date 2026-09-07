package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.BusRequest;
import com.godwintech.gttravels.dto.BusResponse;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.service.AdminBusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/buses")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBusController {

    private final AdminBusService adminBusService;

    public AdminBusController(AdminBusService adminBusService) {

        this.adminBusService = adminBusService;
    }

    // ===========================
    // Get All
    // ===========================

    @GetMapping
    public ResponseEntity<List<BusResponse>> getAllBuses() {

        return ResponseEntity.ok(
                adminBusService.getAllBuses());
    }

    // ===========================
    // Get By Id
    // ===========================

    @GetMapping("/{id}")
    public ResponseEntity<BusResponse> getBus(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminBusService.getBus(id));
    }

    // ===========================
    // Create
    // ===========================

    @PostMapping
    public ResponseEntity<BusResponse> createBus(
            @Valid @RequestBody BusRequest request) {

        return ResponseEntity.ok(
                adminBusService.createBus(request));
    }

    // ===========================
    // Update
    // ===========================

    @PutMapping("/{id}")
    public ResponseEntity<BusResponse> updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusRequest request) {

        return ResponseEntity.ok(
                adminBusService.updateBus(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BusResponse> updateBusStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        BusStatus status = BusStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(adminBusService.updateBusStatus(id, status));
    }

    // ===========================
    // Delete
    // ===========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBus(
            @PathVariable Long id) {

        adminBusService.deleteBus(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Bus deleted successfully"
                ));
    }

}