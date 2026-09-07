package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.*;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BusService {

    private final BusRepository busRepository;
    private final ScheduleRepository scheduleRepository;
    private final BusSearchService busSearchService;
    private final SeatAvailabilityService seatAvailabilityService;
    private final BoardingPointRepository boardingPointRepository;
    private final DroppingPointRepository droppingPointRepository;
    private final RouteStopRepository routeStopRepository;
    private final BusImageRepository busImageRepository;

    public BusService(
            BusRepository busRepository,
            ScheduleRepository scheduleRepository,
            BusSearchService busSearchService,
            SeatAvailabilityService seatAvailabilityService,
            BoardingPointRepository boardingPointRepository,
            DroppingPointRepository droppingPointRepository,
            RouteStopRepository routeStopRepository,
            BusImageRepository busImageRepository) {

        this.busRepository = busRepository;
        this.scheduleRepository = scheduleRepository;
        this.busSearchService = busSearchService;
        this.seatAvailabilityService = seatAvailabilityService;
        this.boardingPointRepository = boardingPointRepository;
        this.droppingPointRepository = droppingPointRepository;
        this.routeStopRepository = routeStopRepository;
        this.busImageRepository = busImageRepository;
    }

    public PageResponse<ScheduleSearchResponse> searchSchedules(BusSearchFilterRequest request) {
        return busSearchService.search(request);
    }

    public List<ScheduleSearchResponse> searchSchedulesLegacy(BusSearchRequest request) {
        BusSearchFilterRequest filter = new BusSearchFilterRequest();
        filter.setSource(request.getSource());
        filter.setDestination(request.getDestination());
        filter.setTravelDate(request.getTravelDate());
        filter.setPage(0);
        filter.setSize(100);
        return busSearchService.search(filter).getContent();
    }

    public BusSeatResponse getBusForSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));

        if (!schedule.isActive() || schedule.getBus().getStatus() != BusStatus.ACTIVE) {
            throw new ResourceNotFoundException("Schedule not available");
        }

        Bus bus = schedule.getBus();
        Map<Long, SeatStatus> statusMap = seatAvailabilityService.getScheduleSeatStatusMap(
                scheduleId, bus.getSeats());

        List<SeatResponse> seats = bus.getSeats().stream()
                .map(seat -> new SeatResponse(
                        seat.getId(),
                        seat.getSeatNumber(),
                        seat.isBookable(),
                        statusMap.getOrDefault(seat.getId(), SeatStatus.AVAILABLE),
                        seat.getRowNumber(),
                        seat.getColumnNumber(),
                        seat.getDeck(),
                        seat.getSeatLayoutType() != null ? seat.getSeatLayoutType().name() : null,
                        seat.getGenderRestriction() != null ? seat.getGenderRestriction().name() : null
                ))
                .toList();

        return new BusSeatResponse(
                bus.getId(),
                bus.getBusNumber(),
                bus.getBusType(),
                schedule.getRoute().getId(),
                schedule.getRoute().getSource(),
                schedule.getRoute().getDestination(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getFare(),
                seats
        );
    }

    public BusDetailResponse getBusDetails(Long busId, Long scheduleId) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        Schedule schedule = scheduleId != null
                ? scheduleRepository.findById(scheduleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"))
                : null;

        if (schedule != null && !schedule.getBus().getId().equals(busId)) {
            throw new ResourceNotFoundException("Schedule does not belong to this bus");
        }

        BusDetailResponse response = new BusDetailResponse();
        response.setBusId(bus.getId());
        response.setBusName(bus.getBusName());
        response.setBusNumber(bus.getBusNumber());
        response.setRegistrationNumber(bus.getRegistrationNumber());
        response.setBusType(bus.getBusType());
        response.setBusCategory(bus.getBusCategory() != null ? bus.getBusCategory().name() : null);
        response.setAcType(bus.getAcType());
        response.setSeatConfiguration(
                bus.getSeatConfiguration() != null ? bus.getSeatConfiguration().name() : null);
        response.setDescription(bus.getDescription());

        if (bus.getOperator() != null) {
            response.setOperatorName(bus.getOperator().getName());
            response.setOperatorId(bus.getOperator().getId());
        }

        response.setAmenities(bus.getAmenities().stream().map(a -> a.getName()).sorted().toList());
        response.setImages(busImageRepository.findByBusIdAndActiveTrueOrderByPrimaryImageDesc(busId).stream()
                .map(img -> new BusImageResponse(img.getId(), img.getImageUrl(), img.isPrimaryImage()))
                .toList());

        if (schedule != null) {
            response.setScheduleId(schedule.getId());
            response.setSource(schedule.getRoute().getSource());
            response.setDestination(schedule.getRoute().getDestination());
            response.setDepartureTime(schedule.getDepartureTime());
            response.setArrivalTime(schedule.getArrivalTime());
            response.setFare(schedule.getFare());
            response.setTripStatus(schedule.getTripStatus() != null ? schedule.getTripStatus().name() : null);
            response.setDurationMinutes(java.time.Duration.between(
                    schedule.getDepartureTime(), schedule.getArrivalTime()).toMinutes());
            response.setAvailableSeats(seatAvailabilityService.countAvailableSeats(
                    schedule.getId(), bus.getSeats()));

            Long routeId = schedule.getRoute().getId();
            response.setRouteStops(routeStopRepository.findByRouteIdAndActiveTrueOrderBySequenceNoAsc(routeId)
                    .stream()
                    .map(s -> new RouteStopResponse(s.getId(), s.getCityName(), s.getStopName(),
                            s.getSequenceNo(), s.getArrivalTime(), s.getDepartureTime(), s.getDistanceKm()))
                    .toList());
            response.setBoardingPoints(boardingPointRepository
                    .findByRoute_IdAndActiveTrueOrderBySequenceNoAsc(routeId).stream()
                    .map(bp -> new BoardingPointResponse(bp.getId(), routeId,
                            schedule.getRoute().getSource() + " - " + schedule.getRoute().getDestination(),
                            bp.getPointName(), bp.getPickupTime(), bp.getSequenceNo(),
                            bp.getAddress(), bp.getLandmark(), bp.getCity()))
                    .toList());
            response.setDroppingPoints(droppingPointRepository
                    .findByRoute_IdAndActiveTrueOrderBySequenceNoAsc(routeId).stream()
                    .map(dp -> new DroppingPointResponse(dp.getId(), routeId,
                            schedule.getRoute().getSource() + " - " + schedule.getRoute().getDestination(),
                            dp.getPointName(), dp.getDropTime(), dp.getSequenceNo(),
                            dp.getAddress(), dp.getLandmark(), dp.getCity()))
                    .toList());
        }

        return response;
    }

    public List<Bus> getOperatorBuses(User operator) {
        return busRepository.findByOperatorId(operator.getId());
    }

    @Transactional
    public Bus addBus(Bus bus, User operator) {
        bus.setOperator(operator);
        if (bus.getStatus() == null) {
            bus.setStatus(BusStatus.ACTIVE);
        }
        bus.initializeSeats();
        return busRepository.save(bus);
    }

    @Transactional
    public void deleteBus(Long busId, User operator) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        if (!bus.getOperator().getId().equals(operator.getId())) {
            throw new com.godwintech.gttravels.exception.ForbiddenException("Not authorized");
        }

        busRepository.delete(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }
}
