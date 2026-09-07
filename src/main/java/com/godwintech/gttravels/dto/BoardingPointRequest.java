package com.godwintech.gttravels.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BoardingPointRequest {

    @NotNull(message = "Route is required")
    private Long routeId;

    @NotBlank(message = "Boarding Point Name is required")
    private String pointName;

    @NotBlank(message = "Pickup Time is required")
    private String pickupTime;

    @NotNull(message = "Sequence is required")
    @Min(value = 1, message = "Sequence must be greater than 0")
    private Integer sequenceNo;

    public BoardingPointRequest() {
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
}