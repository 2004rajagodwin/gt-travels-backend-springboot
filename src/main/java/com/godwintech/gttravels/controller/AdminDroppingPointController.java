package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.DroppingPointRequest;
import com.godwintech.gttravels.dto.DroppingPointResponse;
import com.godwintech.gttravels.service.AdminDroppingPointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dropping-points")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDroppingPointController {

    private final AdminDroppingPointService droppingPointService;

    public AdminDroppingPointController(
            AdminDroppingPointService droppingPointService) {

        this.droppingPointService = droppingPointService;
    }

    // ===========================
    // Get All Dropping Points
    // ===========================

    @GetMapping
    public ResponseEntity<List<DroppingPointResponse>> getAllDroppingPoints() {

        return ResponseEntity.ok(
                droppingPointService.getAllDroppingPoints());
    }

    // ===========================
    // Get Dropping Point By Id
    // ===========================

    @GetMapping("/{id}")
    public ResponseEntity<DroppingPointResponse> getDroppingPoint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                droppingPointService.getDroppingPoint(id));
    }

    // ===========================
    // Create Dropping Point
    // ===========================

    @PostMapping
    public ResponseEntity<DroppingPointResponse> createDroppingPoint(
            @Valid @RequestBody DroppingPointRequest request) {

        return ResponseEntity.ok(
                droppingPointService.createDroppingPoint(request));
    }

    // ===========================
    // Update Dropping Point
    // ===========================

    @PutMapping("/{id}")
    public ResponseEntity<DroppingPointResponse> updateDroppingPoint(
            @PathVariable Long id,
            @Valid @RequestBody DroppingPointRequest request) {

        return ResponseEntity.ok(
                droppingPointService.updateDroppingPoint(id, request));
    }

    // ===========================
    // Delete Dropping Point
    // ===========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDroppingPoint(
            @PathVariable Long id) {

        droppingPointService.deleteDroppingPoint(id);

        return ResponseEntity.ok(
                Map.of("message", "Dropping Point deleted successfully"));
    }
}