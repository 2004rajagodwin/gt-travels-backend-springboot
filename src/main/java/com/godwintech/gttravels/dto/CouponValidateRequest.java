package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CouponValidateRequest {

    @NotBlank
    private String couponCode;

    @NotNull
    private Long scheduleId;

    @NotNull
    private List<Long> seatIds;

    @NotNull
    private Long boardingPointId;

    @NotNull
    private Long droppingPointId;

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public List<Long> getSeatIds() { return seatIds; }
    public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
    public Long getBoardingPointId() { return boardingPointId; }
    public void setBoardingPointId(Long boardingPointId) { this.boardingPointId = boardingPointId; }
    public Long getDroppingPointId() { return droppingPointId; }
    public void setDroppingPointId(Long droppingPointId) { this.droppingPointId = droppingPointId; }
}
