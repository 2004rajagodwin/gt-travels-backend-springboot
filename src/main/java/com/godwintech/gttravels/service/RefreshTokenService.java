package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.dto.RefreshTokenResult;
import com.godwintech.gttravels.dto.SessionResponse;
import com.godwintech.gttravels.entity.RefreshToken;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.repository.RefreshTokenRepository;
import com.godwintech.gttravels.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthProperties authProperties;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               AuthProperties authProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.authProperties = authProperties;
    }

    @Transactional
    public RefreshTokenResult createRefreshToken(User user, String userAgent, String ipAddress) {
        return createRefreshToken(user, UUID.randomUUID().toString(), userAgent, ipAddress);
    }

    @Transactional
    public RefreshTokenResult createRefreshToken(User user, String familyId,
                                                 String userAgent, String ipAddress) {
        String rawToken = TokenUtils.generateSecureToken();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(authProperties.getRefreshExpirationMs() / 1000);

        RefreshToken token = new RefreshToken();
        token.setTokenHash(TokenUtils.hashToken(rawToken));
        token.setUser(user);
        token.setFamilyId(familyId);
        token.setExpiresAt(expiresAt);
        token.setCreatedAt(now);
        token.setUserAgent(userAgent);
        token.setIpAddress(ipAddress);

        refreshTokenRepository.save(token);
        return new RefreshTokenResult(rawToken, authProperties.getRefreshExpirationMs() / 1000);
    }

    @Transactional
    public RefreshTokenResult rotateRefreshToken(String rawToken, String userAgent, String ipAddress) {
        String hash = TokenUtils.hashToken(rawToken);
        RefreshToken existing = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (existing.isRevoked()) {
            refreshTokenRepository.revokeFamily(existing.getFamilyId(), LocalDateTime.now());
            log.warn("Refresh token reuse detected for user {}", existing.getUser().getId());
            throw new BadRequestException("Invalid refresh token");
        }

        if (existing.isExpired()) {
            existing.setRevokedAt(LocalDateTime.now());
            refreshTokenRepository.save(existing);
            throw new BadRequestException("Refresh token expired");
        }

        RefreshTokenResult rotated = createRefreshToken(
                existing.getUser(),
                existing.getFamilyId(),
                userAgent,
                ipAddress);

        existing.setRevokedAt(LocalDateTime.now());
        RefreshToken savedNew = refreshTokenRepository.findByTokenHash(
                TokenUtils.hashToken(rotated.getRawToken())).orElse(null);
        if (savedNew != null) {
            existing.setReplacedById(savedNew.getId());
        }
        refreshTokenRepository.save(existing);

        return rotated;
    }

    @Transactional
    public void revokeToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank()) {
            return;
        }
        refreshTokenRepository.findByTokenHash(TokenUtils.hashToken(rawToken))
                .ifPresent(token -> {
                    token.setRevokedAt(LocalDateTime.now());
                    refreshTokenRepository.save(token);
                });
    }

    @Transactional
    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.revokeAllActiveForUser(userId, LocalDateTime.now());
    }

    public List<SessionResponse> getActiveSessions(User user, String currentTokenHash) {
        return refreshTokenRepository.findByUser_IdAndRevokedAtIsNull(user.getId())
                .stream()
                .filter(token -> !token.isExpired())
                .map(token -> new SessionResponse(
                        token.getId(),
                        token.getUserAgent(),
                        token.getIpAddress(),
                        token.getCreatedAt(),
                        token.getExpiresAt(),
                        currentTokenHash != null
                                && currentTokenHash.equals(token.getTokenHash())))
                .toList();
    }

    public User findUserByRawToken(String rawToken) {
        return refreshTokenRepository.findByTokenHash(TokenUtils.hashToken(rawToken))
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));
    }
}
