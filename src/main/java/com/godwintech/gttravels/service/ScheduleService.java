package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.ScheduleRequest;
import com.godwintech.gttravels.dto.ScheduleResponse;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.Route;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.TripStatus;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.BusRepository;
import com.godwintech.gttravels.repository.RouteRepository;
import com.godwintech.gttravels.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    public ScheduleService(
            ScheduleRepository scheduleRepository,
            BusRepository busRepository,
            RouteRepository routeRepository) {

        this.scheduleRepository = scheduleRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
    }

    public List<ScheduleResponse> getAllSchedules() {
        return scheduleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ScheduleResponse getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        return mapToResponse(schedule);
    }

    public ScheduleResponse createSchedule(ScheduleRequest request) {
        validateScheduleRequest(request);
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        Schedule schedule = new Schedule();
        applyRequest(schedule, request, bus, route);
        return mapToResponse(scheduleRepository.save(schedule));
    }

    public ScheduleResponse updateSchedule(Long id, ScheduleRequest request) {
        validateScheduleRequest(request);
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        applyRequest(schedule, request, bus, route);
        return mapToResponse(scheduleRepository.save(schedule));
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }

    private void validateScheduleRequest(ScheduleRequest request) {
        if (request.getDepartureTime() == null || request.getArrivalTime() == null) {
            throw new BadRequestException("Departure and arrival times are required");
        }
        if (request.getDepartureTime().toLocalDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Past journey date is not allowed");
        }
        if (!request.getArrivalTime().isAfter(request.getDepartureTime())) {
            throw new BadRequestException("Arrival time must be after departure time");
        }
        if (request.getFare() == null || request.getFare() < 0) {
            throw new BadRequestException("Fare must be zero or positive");
        }
        if (request.getBusId() == null || request.getRouteId() == null) {
            throw new BadRequestException("Bus and route are required");
        }

        Bus bus = busRepository.findById(request.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        if (bus.getStatus() != BusStatus.ACTIVE) {
            throw new BadRequestException("Cannot schedule trip for inactive bus");
        }
        routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));
    }

    private void applyRequest(Schedule schedule, ScheduleRequest request, Bus bus, Route route) {
        schedule.setBus(bus);
        schedule.setRoute(route);
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setFare(request.getFare());
        schedule.setActive(request.isActive());
        schedule.setJourneyDate(request.getDepartureTime().toLocalDate());
        if (request.getTripStatus() != null) {
            schedule.setTripStatus(request.getTripStatus());
        } else if (schedule.getTripStatus() == null) {
            schedule.setTripStatus(TripStatus.SCHEDULED);
        }
    }

    private ScheduleResponse mapToResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getBus().getId(),
                schedule.getBus().getBusNumber(),
                schedule.getRoute().getId(),
                schedule.getRoute().getSource(),
                schedule.getRoute().getDestination(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getFare(),
                schedule.isActive(),
                schedule.getJourneyDate(),
                schedule.getTripStatus() != null ? schedule.getTripStatus().name() : null
        );
    }
}
