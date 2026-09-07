package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AccountStatus;
import com.godwintech.gttravels.exception.AccountLockedException;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AccountSecurityService {

    private final UserRepository userRepository;
    private final AuthProperties authProperties;

    public AccountSecurityService(UserRepository userRepository, AuthProperties authProperties) {
        this.userRepository = userRepository;
        this.authProperties = authProperties;
    }

    public void ensureCanAuthenticate(User user) {
        if (user.getAccountStatus() == AccountStatus.DISABLED || !user.isEnabled()) {
            throw new BadRequestException("Account is disabled");
        }

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new AccountLockedException("Account is temporarily locked. Please try again later.");
        }

        if (user.getAccountStatus() == AccountStatus.LOCKED
                && user.getLockedUntil() != null
                && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new AccountLockedException("Account is temporarily locked. Please try again later.");
        }
    }

    @Transactional
    public void recordFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= authProperties.getMaxLoginAttempts()) {
            user.setLockedUntil(LocalDateTime.now().plusMinutes(authProperties.getAccountLockMinutes()));
            user.setAccountStatus(AccountStatus.LOCKED);
        }

        userRepository.save(user);
    }

    @Transactional
    public void resetFailedLogin(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        if (user.getAccountStatus() == AccountStatus.LOCKED) {
            user.setAccountStatus(AccountStatus.ACTIVE);
        }
        userRepository.save(user);
    }
}
