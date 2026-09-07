package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class CouponRequest {

    @NotBlank
    private String code;

    private String description;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @Positive
    private Double discountValue;

    private Double minimumBookingAmount = 0.0;
    private Double maximumDiscount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate expiryDate;

    private Integer usageLimit;
    private Integer perUserLimit = 1;
    private boolean active = true;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
    public Double getMinimumBookingAmount() { return minimumBookingAmount; }
    public void setMinimumBookingAmount(Double minimumBookingAmount) { this.minimumBookingAmount = minimumBookingAmount; }
    public Double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(Double maximumDiscount) { this.maximumDiscount = maximumDiscount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getPerUserLimit() { return perUserLimit; }
    public void setPerUserLimit(Integer perUserLimit) { this.perUserLimit = perUserLimit; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
