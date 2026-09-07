package com.godwintech.gttravels.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BusSeatResponse {

    private Long busId;
    private String busNumber;
    private String busType;

    private Long routeId;

    private String source;
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Double fare;

    private List<SeatResponse> seats;

    public BusSeatResponse() {
    }

    public BusSeatResponse(
            Long busId,
            String busNumber,
            String busType,
            Long routeId,
            String source,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Double fare,
            List<SeatResponse> seats) {

        this.busId = busId;
        this.busNumber = busNumber;
        this.busType = busType;
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.seats = seats;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public List<SeatResponse> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatResponse> seats) {
        this.seats = seats;
    }
}