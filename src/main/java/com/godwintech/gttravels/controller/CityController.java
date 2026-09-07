package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.CityResponse;
import com.godwintech.gttravels.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityResponse>> listActiveCities(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(cityService.searchActiveCities(null, limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CityResponse>> searchCities(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(cityService.searchActiveCities(q, limit));
    }
}
