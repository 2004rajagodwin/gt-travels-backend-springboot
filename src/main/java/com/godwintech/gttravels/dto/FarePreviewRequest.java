package com.godwintech.gttravels.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FarePreviewRequest {

    @NotNull
    private Long scheduleId;

    @NotEmpty
    private List<Long> seatIds;

    @NotNull
    private Long boardingPointId;

    @NotNull
    private Long droppingPointId;

    private String couponCode;

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
