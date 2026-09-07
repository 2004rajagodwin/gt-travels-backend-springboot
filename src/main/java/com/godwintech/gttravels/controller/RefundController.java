package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.RefundResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.RefundService;
import com.godwintech.gttravels.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;
    private final UserService userService;

    public RefundController(RefundService refundService, UserService userService) {
        this.refundService = refundService;
        this.userService = userService;
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RefundResponse> getRefund(@PathVariable String bookingId) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(refundService.getRefundForBooking(bookingId, user));
    }
}
