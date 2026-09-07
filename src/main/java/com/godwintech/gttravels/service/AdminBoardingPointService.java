package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.BoardingPointRequest;
import com.godwintech.gttravels.dto.BoardingPointResponse;
import com.godwintech.gttravels.entity.BoardingPoint;
import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.repository.BoardingPointRepository;
import com.godwintech.gttravels.repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminBoardingPointService {

    private final BoardingPointRepository boardingPointRepository;
    private final RouteRepository routeRepository;

    public AdminBoardingPointService(
            BoardingPointRepository boardingPointRepository,
            RouteRepository routeRepository) {

        this.boardingPointRepository = boardingPointRepository;
        this.routeRepository = routeRepository;
    }

    // ===========================
    // Get All
    // ===========================

    public List<BoardingPointResponse> getAllBoardingPoints() {

        return boardingPointRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===========================
    // Get By Id
    // ===========================

    public BoardingPointResponse getBoardingPoint(Long id) {

        BoardingPoint point = boardingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Boarding Point not found"));

        return mapToResponse(point);
    }

    // ===========================
    // Create
    // ===========================

    public BoardingPointResponse createBoardingPoint(
            BoardingPointRequest request) {

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        BoardingPoint point = new BoardingPoint();

        point.setRoute(route);
        point.setPointName(request.getPointName());
        point.setPickupTime(request.getPickupTime());
        point.setSequenceNo(request.getSequenceNo());

        return mapToResponse(
                boardingPointRepository.save(point));
    }

    // ===========================
    // Update
    // ===========================

    public BoardingPointResponse updateBoardingPoint(
            Long id,
            BoardingPointRequest request) {

        BoardingPoint point = boardingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Boarding Point not found"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        point.setRoute(route);
        point.setPointName(request.getPointName());
        point.setPickupTime(request.getPickupTime());
        point.setSequenceNo(request.getSequenceNo());

        return mapToResponse(
                boardingPointRepository.save(point));
    }

    // ===========================
    // Delete
    // ===========================

    public void deleteBoardingPoint(Long id) {

        BoardingPoint point = boardingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Boarding Point not found"));

        boardingPointRepository.delete(point);
    }

    // ===========================
    // Mapper
    // ===========================

    private BoardingPointResponse mapToResponse(
            BoardingPoint point) {

        return new BoardingPointResponse(

                point.getId(),

                point.getRoute().getId(),

                point.getRoute().getSource()
                        + " → "
                        + point.getRoute().getDestination(),

                point.getPointName(),

                point.getPickupTime(),

                point.getSequenceNo()

        );
    }
}