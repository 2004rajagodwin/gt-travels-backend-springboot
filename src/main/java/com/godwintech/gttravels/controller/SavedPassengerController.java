package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.SavedPassengerRequest;
import com.godwintech.gttravels.dto.SavedPassengerResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.SavedPassengerService;
import com.godwintech.gttravels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/passengers")
@PreAuthorize("hasRole('CUSTOMER')")
public class SavedPassengerController {

    private final SavedPassengerService savedPassengerService;
    private final UserService userService;

    public SavedPassengerController(
            SavedPassengerService savedPassengerService,
            UserService userService) {
        this.savedPassengerService = savedPassengerService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<SavedPassengerResponse>> list() {
        return ResponseEntity.ok(savedPassengerService.listForUser(userService.getCurrentUser()));
    }

    @PostMapping
    public ResponseEntity<SavedPassengerResponse> create(
            @Valid @RequestBody SavedPassengerRequest request) {
        return ResponseEntity.ok(
                savedPassengerService.create(userService.getCurrentUser(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SavedPassengerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SavedPassengerRequest request) {
        return ResponseEntity.ok(
                savedPassengerService.update(userService.getCurrentUser(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        savedPassengerService.delete(userService.getCurrentUser(), id);
        return ResponseEntity.ok(Map.of("message", "Passenger deleted"));
    }
}
