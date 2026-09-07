package com.godwintech.gttravels.entity;

import com.godwintech.gttravels.enums.DiscountType;
import com.godwintech.gttravels.enums.PolicyScopeType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotional_offers")
public class PromotionalOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false)
    private Double discountValue;

    @Column(name = "maximum_discount")
    private Double maximumDiscount;

    @Column(name = "minimum_booking_amount", nullable = false)
    private Double minimumBookingAmount = 0.0;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "scope_type", nullable = false, length = 20)
    private PolicyScopeType scopeType = PolicyScopeType.GLOBAL;

    @Column(name = "scope_id")
    private Long scopeId;

    @Column(name = "first_booking_only", nullable = false)
    private boolean firstBookingOnly = false;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public DiscountType getDiscountType() { return discountType; }
    public void setDiscountType(DiscountType discountType) { this.discountType = discountType; }
    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
    public Double getMaximumDiscount() { return maximumDiscount; }
    public void setMaximumDiscount(Double maximumDiscount) { this.maximumDiscount = maximumDiscount; }
    public Double getMinimumBookingAmount() { return minimumBookingAmount; }
    public void setMinimumBookingAmount(Double minimumBookingAmount) { this.minimumBookingAmount = minimumBookingAmount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public PolicyScopeType getScopeType() { return scopeType; }
    public void setScopeType(PolicyScopeType scopeType) { this.scopeType = scopeType; }
    public Long getScopeId() { return scopeId; }
    public void setScopeId(Long scopeId) { this.scopeId = scopeId; }
    public boolean isFirstBookingOnly() { return firstBookingOnly; }
    public void setFirstBookingOnly(boolean firstBookingOnly) { this.firstBookingOnly = firstBookingOnly; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
