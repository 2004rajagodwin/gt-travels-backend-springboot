package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.BoardingPointResponse;
import com.godwintech.gttravels.repository.BoardingPointRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/boarding-points")
public class BoardingPointController {
    private final BoardingPointRepository boardingPointRepository;

    public BoardingPointController(BoardingPointRepository boardingPointRepository) {
        this.boardingPointRepository = boardingPointRepository;
    }

    @GetMapping("/route/{routeId}")
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardingPointResponse>> getByRoute(
            @PathVariable Long routeId) {
        List<BoardingPointResponse> response = boardingPointRepository
                .findByRoute_IdOrderBySequenceNoAsc(routeId)
                .stream()
                .map(bp -> new BoardingPointResponse(
                        bp.getId(),
                        bp.getRoute().getId(),
                        bp.getRoute().getSource() + " - " + bp.getRoute().getDestination(),
                        bp.getPointName(),
                        bp.getPickupTime(),
                        bp.getSequenceNo()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }
}