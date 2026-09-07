package com.godwintech.gttravels.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DroppingPointRequest {

    @NotNull(message = "Route is required")
    private Long routeId;

    @NotBlank(message = "Dropping Point Name is required")
    private String pointName;

    @NotBlank(message = "Drop Time is required")
    private String dropTime;

    @NotNull(message = "Sequence is required")
    @Min(value = 1, message = "Sequence must be greater than 0")
    private Integer sequenceNo;

    public DroppingPointRequest() {
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

    public String getDropTime() {
        return dropTime;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
}