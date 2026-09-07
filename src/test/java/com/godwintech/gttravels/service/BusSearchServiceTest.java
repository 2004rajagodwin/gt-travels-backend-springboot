package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.BusSearchFilterRequest;
import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.ScheduleSearchResponse;
import com.godwintech.gttravels.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BusSearchServiceTest {

    @Autowired
    private BusSearchService busSearchService;

    @Test
    void search_requiresTravelDate() {
        BusSearchFilterRequest request = new BusSearchFilterRequest();
        request.setSource("Chennai");
        request.setDestination("Bangalore");
        assertThrows(BadRequestException.class, () -> busSearchService.search(request));
    }

    @Test
    void search_rejectsPastDate() {
        BusSearchFilterRequest request = new BusSearchFilterRequest();
        request.setSource("Chennai");
        request.setDestination("Bangalore");
        request.setDate(LocalDate.now().minusDays(1));
        assertThrows(BadRequestException.class, () -> busSearchService.search(request));
    }

    @Test
    void search_returnsPaginatedResponse() {
        BusSearchFilterRequest request = new BusSearchFilterRequest();
        request.setSource("Chennai");
        request.setDestination("Bangalore");
        request.setDate(LocalDate.now().plusDays(1));
        request.setPage(0);
        request.setSize(10);

        PageResponse<ScheduleSearchResponse> result = busSearchService.search(request);
        assertNotNull(result.getContent());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
    }
}
