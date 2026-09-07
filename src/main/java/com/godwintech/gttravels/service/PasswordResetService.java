package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.entity.PasswordResetToken;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AuthProvider;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.repository.PasswordResetTokenRepository;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.util.PasswordValidator;
import com.godwintech.gttravels.util.TokenUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthEmailService authEmailService;
    private final RefreshTokenService refreshTokenService;
    private final AuthProperties authProperties;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                PasswordEncoder passwordEncoder,
                                AuthEmailService authEmailService,
                                RefreshTokenService refreshTokenService,
                                AuthProperties authProperties) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authEmailService = authEmailService;
        this.refreshTokenService = refreshTokenService;
        this.authProperties = authProperties;
    }

    @Transactional
    public void requestPasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getAuthProvider() == AuthProvider.GOOGLE && user.getPassword() == null) {
                return;
            }
            tokenRepository.invalidateActiveTokensForUser(user.getId(), LocalDateTime.now());

            String rawToken = TokenUtils.generateSecureToken();
            LocalDateTime now = LocalDateTime.now();

            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setTokenHash(TokenUtils.hashToken(rawToken));
            token.setCreatedAt(now);
            token.setExpiresAt(now.plusHours(authProperties.getPasswordResetHours()));
            tokenRepository.save(token);

            authEmailService.sendPasswordResetEmail(user, rawToken);
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordValidator.validate(newPassword);

        PasswordResetToken token = tokenRepository
                .findByTokenHash(TokenUtils.hashToken(rawToken))
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (token.isUsed()) {
            throw new BadRequestException("Reset token already used");
        }
        if (token.isExpired()) {
            throw new BadRequestException("Reset token expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);

        refreshTokenService.revokeAllForUser(user.getId());
        authEmailService.sendPasswordChangedEmail(user);
    }
}
