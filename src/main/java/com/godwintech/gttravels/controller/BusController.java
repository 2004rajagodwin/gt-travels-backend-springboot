package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.*;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.AmenityService;
import com.godwintech.gttravels.service.BusService;
import com.godwintech.gttravels.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BusController {

    private final BusService busService;
    private final UserService userService;
    private final AmenityService amenityService;

    public BusController(BusService busService,
                         UserService userService,
                         AmenityService amenityService) {

        this.busService = busService;
        this.userService = userService;
        this.amenityService = amenityService;
    }

    @PostMapping("/buses/search")
    public ResponseEntity<PageResponse<ScheduleSearchResponse>> searchBuses(
            @RequestBody BusSearchFilterRequest request) {
        return ResponseEntity.ok(busService.searchSchedules(request));
    }

    @GetMapping("/buses/{id}")
    public ResponseEntity<BusDetailResponse> getBusDetails(
            @PathVariable Long id,
            @RequestParam(required = false) Long scheduleId) {
        return ResponseEntity.ok(busService.getBusDetails(id, scheduleId));
    }

    @GetMapping("/buses/by-schedule/{scheduleId}")
    public ResponseEntity<BusSeatResponse> getBusForSeatSelection(
            @PathVariable Long scheduleId) {
        return ResponseEntity.ok(busService.getBusForSchedule(scheduleId));
    }

    @GetMapping("/amenities")
    public ResponseEntity<List<AmenityResponse>> listAmenities() {
        return ResponseEntity.ok(amenityService.listActiveAmenities());
    }

    @GetMapping("/operator/buses")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<List<Bus>> getOperatorBuses() {
        User operator = userService.getCurrentUser();
        return ResponseEntity.ok(busService.getOperatorBuses(operator));
    }

    @PostMapping("/operator/buses")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<Bus> addBus(@RequestBody Bus bus) {
        User operator = userService.getCurrentUser();
        return ResponseEntity.ok(busService.addBus(bus, operator));
    }

    @DeleteMapping("/operator/buses/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<Map<String, String>> deleteBus(@PathVariable Long id) {
        User operator = userService.getCurrentUser();
        busService.deleteBus(id, operator);
        return ResponseEntity.ok(Map.of("message", "Bus deleted successfully"));
    }

    @GetMapping("/buses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBuses() {
        return ResponseEntity.ok(
                busService.getAllBuses()
                        .stream()
                        .map(bus -> Map.of(
                                "id", bus.getId(),
                                "busNumber", bus.getBusNumber()))
                        .toList());
    }
}
