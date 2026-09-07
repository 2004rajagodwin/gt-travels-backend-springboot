package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            UPDATE EmailVerificationToken t
            SET t.usedAt = :usedAt
            WHERE t.user.id = :userId AND t.usedAt IS NULL
            """)
    int invalidateActiveTokensForUser(@Param("userId") Long userId, @Param("usedAt") LocalDateTime usedAt);
}
