package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.BoardingPoint;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.DroppingPoint;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.TripStatus;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.repository.BoardingPointRepository;
import com.godwintech.gttravels.repository.DroppingPointRepository;
import com.godwintech.gttravels.repository.ScheduleRepository;
import com.godwintech.gttravels.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BookingValidationService {

    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final BoardingPointRepository boardingPointRepository;
    private final DroppingPointRepository droppingPointRepository;
    private final SeatAvailabilityService seatAvailabilityService;

    public BookingValidationService(
            ScheduleRepository scheduleRepository,
            SeatRepository seatRepository,
            BoardingPointRepository boardingPointRepository,
            DroppingPointRepository droppingPointRepository,
            SeatAvailabilityService seatAvailabilityService) {
        this.scheduleRepository = scheduleRepository;
        this.seatRepository = seatRepository;
        this.boardingPointRepository = boardingPointRepository;
        this.droppingPointRepository = droppingPointRepository;
        this.seatAvailabilityService = seatAvailabilityService;
    }

    public ValidatedBookingContext validate(
            Long scheduleId,
            List<Long> seatIds,
            Long boardingPointId,
            Long droppingPointId) {

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BadRequestException("Schedule not found"));

        validateScheduleBookable(schedule);

        Bus bus = schedule.getBus();
        if (bus.getStatus() != BusStatus.ACTIVE) {
            throw new BadRequestException("Bus is not available for booking");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new BadRequestException("One or more selected seats were not found");
        }

        for (Seat seat : seats) {
            if (!seat.getBus().getId().equals(bus.getId())) {
                throw new BadRequestException("Selected seat does not belong to this bus");
            }
            if (!seat.isBookable()) {
                throw new ConflictException(seat.getSeatNumber() + " is not bookable");
            }
            if (!seatAvailabilityService.isSeatAvailableForSchedule(seat, schedule.getId())) {
                throw new ConflictException(seat.getSeatNumber() + " is no longer available");
            }
        }

        BoardingPoint boardingPoint = boardingPointRepository.findById(boardingPointId)
                .orElseThrow(() -> new BadRequestException("Boarding point not found"));
        DroppingPoint droppingPoint = droppingPointRepository.findById(droppingPointId)
                .orElseThrow(() -> new BadRequestException("Dropping point not found"));

        Long routeId = schedule.getRoute().getId();
        if (!Objects.equals(boardingPoint.getRoute().getId(), routeId)) {
            throw new BadRequestException("Boarding point does not belong to this route");
        }
        if (!Objects.equals(droppingPoint.getRoute().getId(), routeId)) {
            throw new BadRequestException("Dropping point does not belong to this route");
        }
        if (!boardingPoint.isActive()) {
            throw new BadRequestException("Boarding point is inactive");
        }
        if (!droppingPoint.isActive()) {
            throw new BadRequestException("Dropping point is inactive");
        }

        return new ValidatedBookingContext(schedule, bus, seats, boardingPoint, droppingPoint);
    }

    public void validateScheduleBookable(Schedule schedule) {
        if (!schedule.isActive()) {
            throw new BadRequestException("Schedule is not available for booking");
        }
        if (schedule.getTripStatus() == TripStatus.CANCELLED
                || schedule.getTripStatus() == TripStatus.COMPLETED) {
            throw new BadRequestException("Schedule is not available for booking");
        }
        LocalDate journeyDate = schedule.getJourneyDate() != null
                ? schedule.getJourneyDate()
                : schedule.getDepartureTime().toLocalDate();
        if (journeyDate.isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot book for a past journey date");
        }
    }

    public record ValidatedBookingContext(
            Schedule schedule,
            Bus bus,
            List<Seat> seats,
            BoardingPoint boardingPoint,
            DroppingPoint droppingPoint) {
    }
}
