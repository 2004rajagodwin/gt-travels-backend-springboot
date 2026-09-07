package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.PromotionalOffer;
import com.godwintech.gttravels.enums.PolicyScopeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PromotionalOfferRepository extends JpaRepository<PromotionalOffer, Long> {

    @Query("""
            SELECT o FROM PromotionalOffer o
            WHERE o.active = true
            AND o.startDate <= :today AND o.expiryDate >= :today
            AND (o.scopeType = 'GLOBAL'
                OR (o.scopeType = 'OPERATOR' AND o.scopeId = :operatorId)
                OR (o.scopeType = 'ROUTE' AND o.scopeId = :routeId)
                OR (o.scopeType = 'BUS' AND o.scopeId = :busId)
                OR (o.scopeType = 'SCHEDULE' AND o.scopeId = :scheduleId))
            """)
    List<PromotionalOffer> findApplicableOffers(
            @Param("today") LocalDate today,
            @Param("operatorId") Long operatorId,
            @Param("routeId") Long routeId,
            @Param("busId") Long busId,
            @Param("scheduleId") Long scheduleId);

    Page<PromotionalOffer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
