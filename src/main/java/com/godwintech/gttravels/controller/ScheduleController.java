package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.ScheduleRequest;
import com.godwintech.gttravels.dto.ScheduleResponse;
import com.godwintech.gttravels.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operator/schedules")
@PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules()
        );

    }

    // ===========================
    // Get Schedule By Id
    // ===========================

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleService.getScheduleById(id)
        );

    }

    // ===========================
    // Create Schedule
    // ===========================

    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @RequestBody ScheduleRequest request) {

        return ResponseEntity.ok(
                scheduleService.createSchedule(request)
        );

    }

    // ===========================
    // Update Schedule
    // ===========================

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(id, request)
        );

    }

    // ===========================
    // Delete Schedule
    // ===========================

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSchedule(
            @PathVariable Long id) { 

        scheduleService.deleteSchedule(id);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Schedule deleted successfully"
                )
        );

    }

}