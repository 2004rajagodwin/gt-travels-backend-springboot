package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            UPDATE PasswordResetToken t
            SET t.usedAt = :usedAt
            WHERE t.user.id = :userId AND t.usedAt IS NULL
            """)
    int invalidateActiveTokensForUser(@Param("userId") Long userId, @Param("usedAt") LocalDateTime usedAt);
}
