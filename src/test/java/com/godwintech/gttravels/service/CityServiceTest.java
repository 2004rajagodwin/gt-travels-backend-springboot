package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.CityRequest;
import com.godwintech.gttravels.dto.CityResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CityServiceTest {

    @Autowired
    private CityService cityService;

    @BeforeEach
    void seedCities() {
        CityRequest chennai = new CityRequest();
        chennai.setName("Chennai");
        chennai.setState("Tamil Nadu");
        chennai.setCode("MAA");
        cityService.createCity(chennai);

        CityRequest bangalore = new CityRequest();
        bangalore.setName("Bangalore");
        bangalore.setState("Karnataka");
        bangalore.setCode("BLR");
        cityService.createCity(bangalore);
    }

    @Test
    void searchActiveCities_partialMatch() {
        List<CityResponse> cities = cityService.searchActiveCities("chen", 10);
        assertFalse(cities.isEmpty());
        assertTrue(cities.stream().anyMatch(c -> c.getName().equalsIgnoreCase("Chennai")));
    }

    @Test
    void searchActiveCities_caseInsensitive() {
        List<CityResponse> cities = cityService.searchActiveCities("BANG", 10);
        assertTrue(cities.stream().anyMatch(c -> c.getName().equalsIgnoreCase("Bangalore")));
    }
}
