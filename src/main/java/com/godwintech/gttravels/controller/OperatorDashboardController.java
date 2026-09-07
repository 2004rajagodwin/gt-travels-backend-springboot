package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.OperatorDashboardResponse;
import com.godwintech.gttravels.service.OperatorDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operator/dashboard")
@PreAuthorize("hasRole('OPERATOR')")
public class OperatorDashboardController {

    private final OperatorDashboardService dashboardService;

    public OperatorDashboardController(
            OperatorDashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<OperatorDashboardResponse> dashboard() {

        return ResponseEntity.ok(
                dashboardService.dashboard());
    }
}