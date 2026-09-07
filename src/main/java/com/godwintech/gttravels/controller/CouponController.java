package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.CouponValidateRequest;
import com.godwintech.gttravels.dto.CouponValidateResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.CouponService;
import com.godwintech.gttravels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;
    private final UserService userService;

    public CouponController(CouponService couponService, UserService userService) {
        this.couponService = couponService;
        this.userService = userService;
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CouponValidateResponse> validate(@Valid @RequestBody CouponValidateRequest request) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(couponService.validateCoupon(request, user));
    }
}
