package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.CouponRequest;
import com.godwintech.gttravels.dto.CouponResponse;
import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.CouponService;
import com.godwintech.gttravels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/coupons")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCouponController {

    private final CouponService couponService;
    private final UserService userService;

    public AdminCouponController(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<CouponResponse>> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(couponService.search(search, page, size));
    }

    @PostMapping
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CouponRequest request) {
        User admin = userService.getCurrentUser();
        return ResponseEntity.ok(couponService.create(request, admin));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> update(
            @PathVariable Long id, @Valid @RequestBody CouponRequest request) {
        User admin = userService.getCurrentUser();
        return ResponseEntity.ok(couponService.update(id, request, admin));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CouponResponse> updateStatus(
            @PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        User admin = userService.getCurrentUser();
        boolean active = Boolean.TRUE.equals(body.get("active"));
        return ResponseEntity.ok(couponService.updateStatus(id, active, admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
