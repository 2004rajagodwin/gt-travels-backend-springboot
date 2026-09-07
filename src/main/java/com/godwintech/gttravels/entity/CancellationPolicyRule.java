package com.godwintech.gttravels.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cancellation_policy_rules")
public class CancellationPolicyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private CancellationPolicy policy;

    @Column(name = "min_hours_before_journey", nullable = false)
    private Integer minHoursBeforeJourney;

    @Column(name = "refund_percentage", nullable = false)
    private Double refundPercentage;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CancellationPolicy getPolicy() { return policy; }
    public void setPolicy(CancellationPolicy policy) { this.policy = policy; }
    public Integer getMinHoursBeforeJourney() { return minHoursBeforeJourney; }
    public void setMinHoursBeforeJourney(Integer minHoursBeforeJourney) { this.minHoursBeforeJourney = minHoursBeforeJourney; }
    public Double getRefundPercentage() { return refundPercentage; }
    public void setRefundPercentage(Double refundPercentage) { this.refundPercentage = refundPercentage; }
}
