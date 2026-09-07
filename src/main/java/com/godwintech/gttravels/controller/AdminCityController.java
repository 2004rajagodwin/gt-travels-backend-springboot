package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.AmenityResponse;
import com.godwintech.gttravels.dto.CityRequest;
import com.godwintech.gttravels.dto.CityResponse;
import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.service.AmenityService;
import com.godwintech.gttravels.service.CityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCityController {

    private final CityService cityService;
    private final AmenityService amenityService;

    public AdminCityController(CityService cityService, AmenityService amenityService) {
        this.cityService = cityService;
        this.amenityService = amenityService;
    }

    @GetMapping("/cities")
    public ResponseEntity<PageResponse<CityResponse>> listCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(cityService.listCities(page, size, search));
    }

    @PostMapping("/cities")
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.createCity(request));
    }

    @PutMapping("/cities/{id}")
    public ResponseEntity<CityResponse> updateCity(
            @PathVariable Long id,
            @Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(cityService.updateCity(id, request));
    }

    @GetMapping("/amenities")
    public ResponseEntity<java.util.List<AmenityResponse>> listAmenities() {
        return ResponseEntity.ok(amenityService.listActiveAmenities());
    }
}
