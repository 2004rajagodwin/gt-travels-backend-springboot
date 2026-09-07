package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.AuthResponse;
import com.godwintech.gttravels.dto.ChangePasswordRequest;
import com.godwintech.gttravels.dto.EmailRequest;
import com.godwintech.gttravels.dto.LoginRequest;
import com.godwintech.gttravels.dto.RegisterRequest;
import com.godwintech.gttravels.dto.ResetPasswordRequest;
import com.godwintech.gttravels.dto.SessionResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.AuthService;
import com.godwintech.gttravels.service.EmailVerificationService;
import com.godwintech.gttravels.service.PasswordResetService;
import com.godwintech.gttravels.service.RateLimitService;
import com.godwintech.gttravels.service.UserService;
import com.godwintech.gttravels.util.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;
    private final RateLimitService rateLimitService;
    private final UserService userService;

    public AuthController(AuthService authService,
                          EmailVerificationService emailVerificationService,
                          PasswordResetService passwordResetService,
                          RateLimitService rateLimitService,
                          UserService userService) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
        this.passwordResetService = passwordResetService;
        this.rateLimitService = rateLimitService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request,
                                      HttpServletRequest httpRequest) {
        rateLimitService.checkLimit("register:" + httpRequest.getRemoteAddr());
        authService.register(request);
        return ResponseEntity.ok(Map.of(
                "message", "Registration successful. Please verify your email before logging in."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest,
                                              HttpServletResponse httpResponse) {
        rateLimitService.checkLimit("login:" + httpRequest.getRemoteAddr());
        AuthService.AuthResult result = authService.login(request, httpRequest, httpResponse);
        return ResponseEntity.ok(result.getAuthResponse());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest httpRequest,
                                                HttpServletResponse httpResponse) {
        rateLimitService.checkLimit("refresh:" + httpRequest.getRemoteAddr());
        String refreshToken = CookieHelper.extractRefreshToken(httpRequest.getCookies());
        AuthService.AuthResult result = authService.refreshAccessToken(
                refreshToken, httpRequest, httpResponse);
        return ResponseEntity.ok(result.getAuthResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest,
                                    HttpServletResponse httpResponse) {
        String refreshToken = CookieHelper.extractRefreshToken(httpRequest.getCookies());
        authService.logout(refreshToken, httpResponse);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/logout-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutAll(HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) {
        User user = userService.getCurrentUser();
        authService.logoutAll(user, httpResponse);
        return ResponseEntity.ok(Map.of("message", "Logged out from all devices"));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        emailVerificationService.verifyEmail(token);
        return ResponseEntity.ok(Map.of("message", "Email verified successfully"));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody EmailRequest request,
                                                HttpServletRequest httpRequest) {
        rateLimitService.checkLimit("resend:" + httpRequest.getRemoteAddr() + ":" + request.getEmail());
        emailVerificationService.resendVerification(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "message", "If the account exists and is unverified, a verification email has been sent."));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody EmailRequest request,
                                            HttpServletRequest httpRequest) {
        rateLimitService.checkLimit("forgot:" + httpRequest.getRemoteAddr());
        passwordResetService.requestPasswordReset(request.getEmail());
        return ResponseEntity.ok(Map.of(
                "message", "If the email exists, a reset link has been sent."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request,
                                           HttpServletRequest httpRequest) {
        rateLimitService.checkLimit("reset:" + httpRequest.getRemoteAddr());
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset successful. Please login again."));
    }

    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User user = userService.getCurrentUser();
        authService.changePassword(user, request);
        return ResponseEntity.ok(Map.of(
                "message", "Password changed successfully. Please login again."));
    }

    @GetMapping("/sessions")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SessionResponse>> sessions(HttpServletRequest httpRequest) {
        User user = userService.getCurrentUser();
        String refreshToken = CookieHelper.extractRefreshToken(httpRequest.getCookies());
        return ResponseEntity.ok(authService.getSessions(user, refreshToken));
    }
}
