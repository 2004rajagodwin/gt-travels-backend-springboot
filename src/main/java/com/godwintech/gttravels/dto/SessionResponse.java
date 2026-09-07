package com.godwintech.gttravels.dto;

import java.time.LocalDateTime;

public class SessionResponse {

    private Long id;
    private String userAgent;
    private String ipAddress;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean current;

    public SessionResponse(Long id, String userAgent, String ipAddress,
                           LocalDateTime createdAt, LocalDateTime expiresAt, boolean current) {
        this.id = id;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.current = current;
    }

    public Long getId() {
        return id;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isCurrent() {
        return current;
    }
}
