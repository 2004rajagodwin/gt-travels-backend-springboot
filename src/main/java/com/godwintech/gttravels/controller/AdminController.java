package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.CreateOperatorRequest;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepo;
    private final AdminService adminService;

    public AdminController(UserRepository userRepo,
                           AdminService adminService) {

        this.userRepo = userRepo;
        this.adminService = adminService;
    }

    // Create Operator
    @PostMapping("/operators")
    public ResponseEntity<?> createOperator(
            @Valid @RequestBody CreateOperatorRequest request) {

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        adminService.createOperator(request)
                )
        );
    }

    // All Users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Enable / Disable User
    @PutMapping("/users/{id}")
    public ResponseEntity<?> toggleUserEnabled(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(body.getOrDefault("enabled", user.isEnabled()));
        user.setAccountStatus(user.isEnabled()
                ? com.godwintech.gttravels.enums.AccountStatus.ACTIVE
                : com.godwintech.gttravels.enums.AccountStatus.DISABLED);

        userRepo.save(user);

        return ResponseEntity.ok(user);
    }

    // Reports
    @GetMapping("/reports/{type}")
    public ResponseEntity<?> getReport(@PathVariable String type) {

        return ResponseEntity.ok("Report : " + type);
    }

}