package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.SeatResponse;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // All Seats
    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<SeatResponse>> getSeats(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                seatService.getSeatsByBus(busId));
    }

    // Available Seats
    @GetMapping("/available/{busId}")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(
            @PathVariable Long busId) {

        return ResponseEntity.ok(
                seatService.getAvailableSeats(busId));
    }

    // Single Seat
    @GetMapping("/{id}")
    public ResponseEntity<SeatResponse> getSeat(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                seatService.getSeat(id));
    }

    // Update Status
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<SeatResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam SeatStatus status) {

        return ResponseEntity.ok(
                seatService.updateStatus(id, status));
    }
}