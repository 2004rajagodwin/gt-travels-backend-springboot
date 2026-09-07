package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.BusSearchFilterRequest;
import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.ScheduleSearchResponse;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.TripStatus;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.repository.ScheduleRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BusSearchService {

    private final ScheduleRepository scheduleRepository;
    private final SeatAvailabilityService seatAvailabilityService;

    public BusSearchService(
            ScheduleRepository scheduleRepository,
            SeatAvailabilityService seatAvailabilityService) {
        this.scheduleRepository = scheduleRepository;
        this.seatAvailabilityService = seatAvailabilityService;
    }

    public PageResponse<ScheduleSearchResponse> search(BusSearchFilterRequest request) {
        LocalDate travelDate = request.getDate();
        if (travelDate == null) {
            throw new BadRequestException("Travel date is required");
        }
        if (travelDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Past journey date is not allowed");
        }

        String source = request.getSource();
        String destination = request.getDestination();
        if (source == null || source.isBlank() || destination == null || destination.isBlank()) {
            throw new BadRequestException("Source and destination are required");
        }

        int page = Math.max(request.getPage(), 0);
        int size = request.getSize() <= 0 ? 10 : Math.min(request.getSize(), 50);
        Pageable pageable = PageRequest.of(page, size, resolveSort(request.getSort()));

        Specification<Schedule> spec = buildSpecification(request, travelDate, source, destination);
        Page<Schedule> schedulePage = scheduleRepository.findAll(spec, pageable);

        List<ScheduleSearchResponse> content = schedulePage.getContent().stream()
                .map(this::mapToResponse)
                .toList();

        return new PageResponse<>(
                content,
                schedulePage.getNumber(),
                schedulePage.getSize(),
                schedulePage.getTotalElements(),
                schedulePage.getTotalPages());
    }

    private Specification<Schedule> buildSpecification(
            BusSearchFilterRequest request,
            LocalDate travelDate,
            String source,
            String destination) {

        return (root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            Join<Schedule, Bus> bus = root.join("bus", JoinType.INNER);
            Join<Schedule, com.godwintech.gttravels.entity.Route> route = root.join("route", JoinType.INNER);

            predicates.add(cb.equal(root.get("active"), true));
            predicates.add(cb.equal(root.get("journeyDate"), travelDate));
            predicates.add(cb.not(root.get("tripStatus").in(
                    TripStatus.CANCELLED, TripStatus.COMPLETED)));
            predicates.add(cb.equal(cb.lower(route.get("source")), source.toLowerCase()));
            predicates.add(cb.equal(cb.lower(route.get("destination")), destination.toLowerCase()));
            predicates.add(cb.equal(bus.get("status"), BusStatus.ACTIVE));

            if (request.getBusType() != null) {
                predicates.add(cb.equal(bus.get("busCategory"), request.getBusType()));
            }
            if (request.getSeatType() != null) {
                predicates.add(cb.equal(bus.get("seatConfiguration"), request.getSeatType()));
            }
            if (request.getAc() != null && !request.getAc().isBlank()) {
                predicates.add(cb.equal(cb.lower(bus.get("acType")), request.getAc().toLowerCase()));
            }
            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fare"), request.getMinPrice()));
            }
            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fare"), request.getMaxPrice()));
            }
            if (request.getOperator() != null) {
                predicates.add(cb.equal(bus.get("operator").get("id"), request.getOperator()));
            }
            applyDepartureWindow(predicates, cb, root, request.getDepartureFrom(), request.getDepartureTo());

            if (request.getAmenities() != null && !request.getAmenities().isEmpty()) {
                for (Long amenityId : request.getAmenities()) {
                    predicates.add(cb.equal(
                            bus.join("amenities", JoinType.INNER).get("id"), amenityId));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private void applyDepartureWindow(
            List<Predicate> predicates,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<Schedule> root,
            String departureFrom,
            String departureTo) {

        if (departureFrom == null && departureTo == null) {
            return;
        }
        if (departureFrom != null && !departureFrom.isBlank()) {
            LocalTime from = LocalTime.parse(departureFrom);
            predicates.add(cb.greaterThanOrEqualTo(
                    root.get("departureTime").as(LocalTime.class), from));
        }
        if (departureTo != null && !departureTo.isBlank()) {
            LocalTime to = LocalTime.parse(departureTo);
            predicates.add(cb.lessThan(root.get("departureTime").as(LocalTime.class), to));
        }
    }

    private Sort resolveSort(String sort) {
        if (sort == null) {
            return Sort.by(Sort.Direction.ASC, "departureTime");
        }
        return switch (sort) {
            case "price-low" -> Sort.by(Sort.Direction.ASC, "fare");
            case "price-high" -> Sort.by(Sort.Direction.DESC, "fare");
            case "departure-early" -> Sort.by(Sort.Direction.ASC, "departureTime");
            case "departure-late" -> Sort.by(Sort.Direction.DESC, "departureTime");
            case "duration-short" -> Sort.by(Sort.Direction.ASC, "arrivalTime");
            default -> Sort.by(Sort.Direction.ASC, "departureTime");
        };
    }

    private ScheduleSearchResponse mapToResponse(Schedule schedule) {
        Bus bus = schedule.getBus();
        int available = seatAvailabilityService.countAvailableSeats(schedule.getId(), bus.getSeats());

        long durationMinutes = java.time.Duration.between(
                schedule.getDepartureTime(), schedule.getArrivalTime()).toMinutes();

        List<String> amenityNames = bus.getAmenities().stream()
                .map(a -> a.getName())
                .sorted()
                .toList();

        return new ScheduleSearchResponse(
                schedule.getId(),
                bus.getId(),
                bus.getBusNumber(),
                bus.getBusType(),
                schedule.getRoute().getSource(),
                schedule.getRoute().getDestination(),
                schedule.getDepartureTime(),
                schedule.getArrivalTime(),
                schedule.getFare(),
                available,
                bus.getBusName(),
                bus.getBusCategory() != null ? bus.getBusCategory().name() : null,
                bus.getAcType(),
                bus.getSeatConfiguration() != null ? bus.getSeatConfiguration().name() : null,
                bus.getOperator() != null ? bus.getOperator().getName() : null,
                bus.getOperator() != null ? bus.getOperator().getId() : null,
                amenityNames,
                durationMinutes,
                schedule.getTripStatus() != null ? schedule.getTripStatus().name() : null
        );
    }
}
