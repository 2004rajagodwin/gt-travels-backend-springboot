package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.CityRequest;
import com.godwintech.gttravels.dto.CityResponse;
import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.entity.City;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<CityResponse> searchActiveCities(String query, int limit) {
        int size = limit <= 0 ? 10 : Math.min(limit, 50);
        Pageable pageable = PageRequest.of(0, size);

        List<City> cities = (query == null || query.isBlank())
                ? cityRepository.findByActiveTrueOrderByNameAsc(pageable)
                : cityRepository.searchActiveByName(query.trim(), pageable);

        return cities.stream().map(this::toResponse).toList();
    }

    public PageResponse<CityResponse> listCities(int page, int size, String search) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("name"));
        Page<City> result = (search == null || search.isBlank())
                ? cityRepository.findAll(pageable)
                : cityRepository.findAll((root, query, cb) ->
                        cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"), pageable);

        List<CityResponse> content = result.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    public CityResponse createCity(CityRequest request) {
        if (cityRepository.existsByNameAndState(request.getName(), request.getState())) {
            throw new BadRequestException("City already exists in this state");
        }
        City city = new City();
        apply(city, request);
        return toResponse(cityRepository.save(city));
    }

    public CityResponse updateCity(Long id, CityRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        apply(city, request);
        return toResponse(cityRepository.save(city));
    }

    private void apply(City city, CityRequest request) {
        city.setName(request.getName());
        city.setState(request.getState());
        city.setCode(request.getCode());
        city.setActive(request.isActive());
    }

    private CityResponse toResponse(City city) {
        return new CityResponse(city.getId(), city.getName(), city.getState(), city.getCode(), city.isActive());
    }
}
