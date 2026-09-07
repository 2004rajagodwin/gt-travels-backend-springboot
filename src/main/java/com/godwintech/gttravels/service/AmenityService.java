package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.AmenityResponse;
import com.godwintech.gttravels.repository.AmenityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AmenityService {

    private final AmenityRepository amenityRepository;

    public AmenityService(AmenityRepository amenityRepository) {
        this.amenityRepository = amenityRepository;
    }

    public List<AmenityResponse> listActiveAmenities() {
        return amenityRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(a -> new AmenityResponse(a.getId(), a.getCode(), a.getName()))
                .toList();
    }
}
