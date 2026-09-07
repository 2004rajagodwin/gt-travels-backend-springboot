package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findByUser_IdAndRevokedAtIsNull(Long userId);

    @Modifying
    @Query("""
            UPDATE RefreshToken rt
            SET rt.revokedAt = :revokedAt
            WHERE rt.user.id = :userId AND rt.revokedAt IS NULL
            """)
    int revokeAllActiveForUser(@Param("userId") Long userId, @Param("revokedAt") LocalDateTime revokedAt);

    @Modifying
    @Query("""
            UPDATE RefreshToken rt
            SET rt.revokedAt = :revokedAt
            WHERE rt.familyId = :familyId AND rt.revokedAt IS NULL
            """)
    int revokeFamily(@Param("familyId") String familyId, @Param("revokedAt") LocalDateTime revokedAt);
}
