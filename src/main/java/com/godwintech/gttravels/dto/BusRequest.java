package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.IndianBusType;
import com.godwintech.gttravels.enums.SeatConfigurationType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class BusRequest {

    @NotBlank(message = "Bus Number is required")
    private String busNumber;

    private String busName;
    private String registrationNumber;
    private String acType;

    @NotBlank(message = "Bus Type is required")
    private String busType;

    private IndianBusType busCategory;
    private SeatConfigurationType seatConfiguration;
    private BusStatus status;
    private String description;

    @Min(value = 10, message = "Minimum 10 seats")
    @Max(value = 60, message = "Maximum 60 seats")
    private Integer totalSeats;

    private Long operatorId;
    private List<Long> amenityIds;
    private List<BusImageRequest> images;

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

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public IndianBusType getBusCategory() {
        return busCategory;
    }

    public void setBusCategory(IndianBusType busCategory) {
        this.busCategory = busCategory;
    }

    public SeatConfigurationType getSeatConfiguration() {
        return seatConfiguration;
    }

    public void setSeatConfiguration(SeatConfigurationType seatConfiguration) {
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

    public List<Long> getAmenityIds() {
        return amenityIds;
    }

    public void setAmenityIds(List<Long> amenityIds) {
        this.amenityIds = amenityIds;
    }

    public List<BusImageRequest> getImages() {
        return images;
    }

    public void setImages(List<BusImageRequest> images) {
        this.images = images;
    }
}
