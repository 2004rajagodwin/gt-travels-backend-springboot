package com.godwintech.gttravels.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleSearchResponse {

    private Long scheduleId;
    private Long busId;
    private String busNumber;
    private String busType;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double fare;
    private Integer availableSeats;
    private String busName;
    private String busCategory;
    private String acType;
    private String seatConfiguration;
    private String operatorName;
    private Long operatorId;
    private List<String> amenities;
    private Long durationMinutes;
    private String tripStatus;
    private Double rating;

    public ScheduleSearchResponse() {
    }

    public ScheduleSearchResponse(
            Long scheduleId,
            Long busId,
            String busNumber,
            String busType,
            String source,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Double fare,
            Integer availableSeats) {
        this(scheduleId, busId, busNumber, busType, source, destination,
                departureTime, arrivalTime, fare, availableSeats,
                null, null, null, null, null, null, null, null, null);
    }

    public ScheduleSearchResponse(
            Long scheduleId,
            Long busId,
            String busNumber,
            String busType,
            String source,
            String destination,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime,
            Double fare,
            Integer availableSeats,
            String busName,
            String busCategory,
            String acType,
            String seatConfiguration,
            String operatorName,
            Long operatorId,
            List<String> amenities,
            Long durationMinutes,
            String tripStatus) {
        this.scheduleId = scheduleId;
        this.busId = busId;
        this.busNumber = busNumber;
        this.busType = busType;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.fare = fare;
        this.availableSeats = availableSeats;
        this.busName = busName;
        this.busCategory = busCategory;
        this.acType = acType;
        this.seatConfiguration = seatConfiguration;
        this.operatorName = operatorName;
        this.operatorId = operatorId;
        this.amenities = amenities;
        this.durationMinutes = durationMinutes;
        this.tripStatus = tripStatus;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
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

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
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

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
