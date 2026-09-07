package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.BusStatus;

import java.util.List;

public class BusResponse {

    private Long id;
    private String busNumber;
    private String busName;
    private String registrationNumber;
    private String busType;
    private String busCategory;
    private String acType;
    private String seatConfiguration;
    private BusStatus status;
    private String description;
    private Integer totalSeats;
    private Long operatorId;
    private String operatorName;
    private List<String> amenities;
    private List<BusImageResponse> images;

    public BusResponse() {
    }

    public BusResponse(Long id, String busNumber, String busType, Integer totalSeats,
                       Long operatorId, String operatorName) {
        this.id = id;
        this.busNumber = busNumber;
        this.busType = busType;
        this.totalSeats = totalSeats;
        this.operatorId = operatorId;
        this.operatorName = operatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
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

    public BusStatus getStatus() {
        return status;
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
}
