	package com.godwintech.gttravels.controller;
	
	import com.godwintech.gttravels.dto.AdminDashboardResponse;
	import com.godwintech.gttravels.service.AdminDashboardService;
	import org.springframework.http.ResponseEntity;
	import org.springframework.security.access.prepost.PreAuthorize;
	import org.springframework.web.bind.annotation.*;
	
	@RestController
	@RequestMapping("/api/admin/dashboard")
	@PreAuthorize("hasRole('ADMIN')")
	public class AdminDashboardController {
	
	    private final AdminDashboardService dashboardService;
	
	    public AdminDashboardController(
	            AdminDashboardService dashboardService) {
	
	        this.dashboardService = dashboardService;
	    }
	
	    @GetMapping
	    public ResponseEntity<AdminDashboardResponse> dashboard() {
	
	        return ResponseEntity.ok(
	                dashboardService.getDashboard()
	        );
	    }
	}