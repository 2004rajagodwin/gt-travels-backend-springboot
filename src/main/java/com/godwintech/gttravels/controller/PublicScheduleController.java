package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.BusDetailResponse;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.ScheduleRepository;
import com.godwintech.gttravels.service.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
public class PublicScheduleController {

    private final BusService busService;
    private final ScheduleRepository scheduleRepository;

    public PublicScheduleController(BusService busService, ScheduleRepository scheduleRepository) {
        this.busService = busService;
        this.scheduleRepository = scheduleRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusDetailResponse> getScheduleDetails(@PathVariable Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return ResponseEntity.ok(busService.getBusDetails(schedule.getBus().getId(), id));
    }
}
