package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.DroppingPointRequest;
import com.godwintech.gttravels.dto.DroppingPointResponse;
import com.godwintech.gttravels.entity.DroppingPoint;
import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.repository.DroppingPointRepository;
import com.godwintech.gttravels.repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AdminDroppingPointService {

    private final DroppingPointRepository droppingPointRepository;
    private final RouteRepository routeRepository;

    public AdminDroppingPointService(
            DroppingPointRepository droppingPointRepository,
            RouteRepository routeRepository) {

        this.droppingPointRepository = droppingPointRepository;
        this.routeRepository = routeRepository;
    }

    // ===========================
    // Get All
    // ===========================

    public List<DroppingPointResponse> getAllDroppingPoints() {

        return droppingPointRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===========================
    // Get By Id
    // ===========================

    public DroppingPointResponse getDroppingPoint(Long id) {

        DroppingPoint point = droppingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dropping Point not found"));

        return mapToResponse(point);
    }

    // ===========================
    // Create
    // ===========================

    public DroppingPointResponse createDroppingPoint(
            DroppingPointRequest request) {

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        DroppingPoint point = new DroppingPoint();

        point.setRoute(route);
        point.setPointName(request.getPointName());
        point.setDropTime(request.getDropTime());
        point.setSequenceNo(request.getSequenceNo());

        return mapToResponse(
                droppingPointRepository.save(point));
    }

    // ===========================
    // Update
    // ===========================

    public DroppingPointResponse updateDroppingPoint(
            Long id,
            DroppingPointRequest request) {

        DroppingPoint point = droppingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dropping Point not found"));

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() ->
                        new RuntimeException("Route not found"));

        point.setRoute(route);
        point.setPointName(request.getPointName());
        point.setDropTime(request.getDropTime());
        point.setSequenceNo(request.getSequenceNo());

        return mapToResponse(
                droppingPointRepository.save(point));
    }

    // ===========================
    // Delete
    // ===========================

    public void deleteDroppingPoint(Long id) {

        DroppingPoint point = droppingPointRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Dropping Point not found"));

        droppingPointRepository.delete(point);
    }

    // ===========================
    // Mapper
    // ===========================

    private DroppingPointResponse mapToResponse(
            DroppingPoint point) {

        return new DroppingPointResponse(

                point.getId(),

                point.getRoute().getId(),

                point.getRoute().getSource()
                        + " → "
                        + point.getRoute().getDestination(),

                point.getPointName(),

                point.getDropTime(),

                point.getSequenceNo()

        );
    }
}