package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.SeatResponse;
import com.godwintech.gttravels.entity.Seat;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    // All Seats
    public List<SeatResponse> getSeatsByBus(Long busId) {

        return seatRepository.findByBusId(busId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Available Seats
    public List<SeatResponse> getAvailableSeats(Long busId) {

        return seatRepository.findByBusIdAndStatus(
                        busId,
                        SeatStatus.AVAILABLE)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Single Seat
    public SeatResponse getSeat(Long id) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        return mapToResponse(seat);
    }

    // Update Status
    public SeatResponse updateStatus(Long id,
                                     SeatStatus status) {

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Seat not found"));

        seat.setStatus(status);

        seat = seatRepository.save(seat);

        return mapToResponse(seat);
    }

    private SeatResponse mapToResponse(Seat seat) {

        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.isBookable(),
                seat.getStatus()
        );
    }
}