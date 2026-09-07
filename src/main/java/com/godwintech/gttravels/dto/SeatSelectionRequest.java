package com.godwintech.gttravels.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class SeatSelectionRequest {

    @NotNull(message = "scheduleId must not be null")
    private Long scheduleId;

    @NotEmpty(message = "seatIds must not be empty")
    private List<Long> seatIds;

    @NotNull(message = "boardingPointId must not be null")
    private Long boardingPointId;

    @NotNull(message = "droppingPointId must not be null")
    private Long droppingPointId;

    private String couponCode;

    public SeatSelectionRequest() {
    }

    public SeatSelectionRequest(Long scheduleId,
                                List<Long> seatIds,
                                Long boardingPointId,
                                Long droppingPointId) {
        this.scheduleId = scheduleId;
        this.seatIds = seatIds;
        this.boardingPointId = boardingPointId;
        this.droppingPointId = droppingPointId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }

    public Long getBoardingPointId() {
        return boardingPointId;
    }

    public void setBoardingPointId(Long boardingPointId) {
        this.boardingPointId = boardingPointId;
    }

    public Long getDroppingPointId() {
        return droppingPointId;
    }

    public void setDroppingPointId(Long droppingPointId) {
        this.droppingPointId = droppingPointId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}