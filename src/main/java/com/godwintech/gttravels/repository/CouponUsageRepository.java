package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    long countByCouponIdAndUserId(Long couponId, Long userId);

    @Query("""
            SELECT COUNT(cu) FROM CouponUsage cu
            WHERE cu.coupon.id = :couponId AND cu.booking IS NOT NULL
            """)
    long countConfirmedUsages(@Param("couponId") Long couponId);
}
