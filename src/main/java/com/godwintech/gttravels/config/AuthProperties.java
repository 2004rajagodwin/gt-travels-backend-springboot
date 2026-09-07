package com.godwintech.gttravels.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthProperties {

    @Value("${app.jwt.access-expiration:900000}")
    private long accessExpirationMs;

    @Value("${app.jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs;

    @Value("${app.security.max-login-attempts:5}")
    private int maxLoginAttempts;

    @Value("${app.security.account-lock-minutes:15}")
    private int accountLockMinutes;

    @Value("${app.security.email-verification-hours:24}")
    private int emailVerificationHours;

    @Value("${app.security.password-reset-hours:1}")
    private int passwordResetHours;

    @Value("${app.security.rate-limit-per-minute:10}")
    private int rateLimitPerMinute;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Value("${app.security.cookie-secure:false}")
    private boolean cookieSecure;

    public long getAccessExpirationMs() {
        return accessExpirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public int getAccountLockMinutes() {
        return accountLockMinutes;
    }

    public int getEmailVerificationHours() {
        return emailVerificationHours;
    }

    public int getPasswordResetHours() {
        return passwordResetHours;
    }

    public int getRateLimitPerMinute() {
        return rateLimitPerMinute;
    }

    public String getFrontendUrl() {
        return frontendUrl;
    }

    public boolean isCookieSecure() {
        return cookieSecure;
    }
}
