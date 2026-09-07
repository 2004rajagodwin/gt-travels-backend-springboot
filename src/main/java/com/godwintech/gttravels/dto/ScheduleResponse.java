package com.godwintech.gttravels.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduleResponse {

    private Long id;

    private Long busId;
    private String busNumber;

    private Long routeId;
    private String source;
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Double fare;

    private boolean active;
    private LocalDate journeyDate;
    private String tripStatus;

    public ScheduleResponse() {
    }

    public ScheduleResponse(
            Long id,
            Long busId,
            String busNumber,
            Long routeId,
            String source,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Double fare,
            boolean active) {
        this(id, busId, busNumber, routeId, source, destination,
                departureTime, arrivalTime, fare, active, null, null);
    }

    public ScheduleResponse(
            Long id,
            Long busId,
            String busNumber,
            Long routeId,
            String source,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Double fare,
            boolean active,
            LocalDate journeyDate,
            String tripStatus) {

        this.id = id;
        this.busId = busId;
        this.busNumber = busNumber;
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.active = active;
        this.journeyDate = journeyDate;
        this.tripStatus = tripStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getBusId() {
        return busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Double getFare() {
        return fare;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }
}