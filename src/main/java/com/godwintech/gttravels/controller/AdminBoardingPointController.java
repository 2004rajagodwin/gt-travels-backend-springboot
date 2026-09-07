package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.BoardingPointRequest;
import com.godwintech.gttravels.dto.BoardingPointResponse;
import com.godwintech.gttravels.service.AdminBoardingPointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/boarding-points")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBoardingPointController {

    private final AdminBoardingPointService boardingPointService;

    public AdminBoardingPointController(
            AdminBoardingPointService boardingPointService) {

        this.boardingPointService = boardingPointService;
    }

    // ===========================
    // Get All Boarding Points
    // ===========================

    @GetMapping
    public ResponseEntity<List<BoardingPointResponse>> getAllBoardingPoints() {

        return ResponseEntity.ok(
                boardingPointService.getAllBoardingPoints());
    }

    // ===========================
    // Get Boarding Point By Id
    // ===========================

    @GetMapping("/{id}")
    public ResponseEntity<BoardingPointResponse> getBoardingPoint(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                boardingPointService.getBoardingPoint(id));
    }

    // ===========================
    // Create Boarding Point
    // ===========================

    @PostMapping
    public ResponseEntity<BoardingPointResponse> createBoardingPoint(
            @Valid @RequestBody BoardingPointRequest request) {

        return ResponseEntity.ok(
                boardingPointService.createBoardingPoint(request));
    }

    // ===========================
    // Update Boarding Point
    // ===========================

    @PutMapping("/{id}")
    public ResponseEntity<BoardingPointResponse> updateBoardingPoint(
            @PathVariable Long id,
            @Valid @RequestBody BoardingPointRequest request) {

        return ResponseEntity.ok(
                boardingPointService.updateBoardingPoint(id, request));
    }

    // ===========================
    // Delete Boarding Point
    // ===========================

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoardingPoint(
            @PathVariable Long id) {

        boardingPointService.deleteBoardingPoint(id);

        return ResponseEntity.ok(
                Map.of("message", "Boarding Point deleted successfully"));
    }
}