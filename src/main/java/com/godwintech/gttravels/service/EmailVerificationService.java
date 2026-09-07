package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.entity.EmailVerificationToken;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AccountStatus;
import com.godwintech.gttravels.enums.AuthProvider;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.repository.EmailVerificationTokenRepository;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.util.TokenUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthEmailService authEmailService;
    private final AuthProperties authProperties;

    public EmailVerificationService(EmailVerificationTokenRepository tokenRepository,
                                    UserRepository userRepository,
                                    AuthEmailService authEmailService,
                                    AuthProperties authProperties) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.authEmailService = authEmailService;
        this.authProperties = authProperties;
    }

    @Transactional
    public void createAndSendVerificationToken(User user) {
        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            return;
        }

        tokenRepository.invalidateActiveTokensForUser(user.getId(), LocalDateTime.now());

        String rawToken = TokenUtils.generateSecureToken();
        LocalDateTime now = LocalDateTime.now();

        EmailVerificationToken token = new EmailVerificationToken();
        token.setUser(user);
        token.setTokenHash(TokenUtils.hashToken(rawToken));
        token.setCreatedAt(now);
        token.setExpiresAt(now.plusHours(authProperties.getEmailVerificationHours()));
        tokenRepository.save(token);

        authEmailService.sendVerificationEmail(user, rawToken);
    }

    @Transactional
    public void verifyEmail(String rawToken) {
        EmailVerificationToken token = tokenRepository
                .findByTokenHash(TokenUtils.hashToken(rawToken))
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (token.isUsed()) {
            throw new BadRequestException("Verification token already used");
        }
        if (token.isExpired()) {
            throw new BadRequestException("Verification token expired");
        }

        User user = token.getUser();
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);

        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    @Transactional
    public void resendVerification(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.isEmailVerified()) {
                return;
            }
            if (user.getAuthProvider() == AuthProvider.GOOGLE) {
                return;
            }
            createAndSendVerificationToken(user);
        });
    }
}
