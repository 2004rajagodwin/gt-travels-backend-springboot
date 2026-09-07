package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.DiscountType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CouponResponse {

    private Long id;
    private String code;
    private String description;
    private DiscountType discountType;
    private Double discountValue;
    private Double minimumBookingAmount;
    private Double maximumDiscount;
    private LocalDate startDate;
    private LocalDate expiryDate;
    private Integer usageLimit;
    private Integer perUserLimit;
    private Integer usedCount;
    private boolean active;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Integer getUsedCount() { return usedCount; }
    public void setUsedCount(Integer usedCount) { this.usedCount = usedCount; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
