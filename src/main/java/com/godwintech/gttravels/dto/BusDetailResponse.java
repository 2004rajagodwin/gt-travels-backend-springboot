package com.godwintech.gttravels.dto;

import java.time.LocalDateTime;
import java.util.List;

public class BusDetailResponse {

    private Long busId;
    private Long scheduleId;
    private String busName;
    private String busNumber;
    private String registrationNumber;
    private String busType;
    private String busCategory;
    private String acType;
    private String seatConfiguration;
    private String description;
    private String operatorName;
    private Long operatorId;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Long durationMinutes;
    private Double fare;
    private Integer availableSeats;
    private List<String> amenities;
    private List<BusImageResponse> images;
    private List<RouteStopResponse> routeStops;
    private List<BoardingPointResponse> boardingPoints;
    private List<DroppingPointResponse> droppingPoints;
    private String tripStatus;

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusCategory() {
        return busCategory;
    }

    public void setBusCategory(String busCategory) {
        this.busCategory = busCategory;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getSeatConfiguration() {
        return seatConfiguration;
    }

    public void setSeatConfiguration(String seatConfiguration) {
        this.seatConfiguration = seatConfiguration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
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

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<BusImageResponse> getImages() {
        return images;
    }

    public void setImages(List<BusImageResponse> images) {
        this.images = images;
    }

    public List<RouteStopResponse> getRouteStops() {
        return routeStops;
    }

    public void setRouteStops(List<RouteStopResponse> routeStops) {
        this.routeStops = routeStops;
    }

    public List<BoardingPointResponse> getBoardingPoints() {
        return boardingPoints;
    }

    public void setBoardingPoints(List<BoardingPointResponse> boardingPoints) {
        this.boardingPoints = boardingPoints;
    }

    public List<DroppingPointResponse> getDroppingPoints() {
        return droppingPoints;
    }

    public void setDroppingPoints(List<DroppingPointResponse> droppingPoints) {
        this.droppingPoints = droppingPoints;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }
}
