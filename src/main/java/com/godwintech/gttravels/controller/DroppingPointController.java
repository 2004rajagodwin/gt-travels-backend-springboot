package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.DroppingPointResponse;
import com.godwintech.gttravels.repository.DroppingPointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dropping-points")
public class DroppingPointController {
    private final DroppingPointRepository droppingPointRepository;

    public DroppingPointController(DroppingPointRepository droppingPointRepository) {
        this.droppingPointRepository = droppingPointRepository;
    }

    @GetMapping("/route/{routeId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<DroppingPointResponse>> getByRoute(
            @PathVariable Long routeId) {
        List<DroppingPointResponse> response = droppingPointRepository
                .findByRoute_IdOrderBySequenceNoAsc(routeId)
                .stream()
                .map(dp -> new DroppingPointResponse(
                        dp.getId(),
                        dp.getRoute().getId(),
                        dp.getRoute().getSource() + " - " + dp.getRoute().getDestination(),
                        dp.getPointName(),
                        dp.getDropTime(),
                        dp.getSequenceNo()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }
}