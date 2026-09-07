package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.RefundResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.RefundService;
import com.godwintech.gttravels.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/refunds")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRefundController {

    private final RefundService refundService;

    public AdminRefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<RefundResponse>> list(
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(refundService.searchAdminRefunds(pnr, reference, status, page, size));
    }
}
