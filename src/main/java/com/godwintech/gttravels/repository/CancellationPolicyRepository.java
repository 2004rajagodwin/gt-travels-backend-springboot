package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.CancellationPolicy;
import com.godwintech.gttravels.enums.PolicyScopeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Long> {

    @Query("""
            SELECT p FROM CancellationPolicy p
            LEFT JOIN FETCH p.rules
            WHERE p.active = true
            AND (p.scopeType = :scopeType AND (p.scopeId = :scopeId OR (:scopeId IS NULL AND p.scopeId IS NULL)))
            """)
    Optional<CancellationPolicy> findActiveByScope(
            @Param("scopeType") PolicyScopeType scopeType,
            @Param("scopeId") Long scopeId);

    List<CancellationPolicy> findByActiveTrue();
}
