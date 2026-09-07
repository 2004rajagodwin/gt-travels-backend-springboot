package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.PaymentSummaryResponse;
import com.godwintech.gttravels.service.AdminPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/payments")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    public AdminPaymentController(AdminPaymentService adminPaymentService) {
        this.adminPaymentService = adminPaymentService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<PaymentSummaryResponse>> list(
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adminPaymentService.search(pnr, status, date, page, size));
    }
}
