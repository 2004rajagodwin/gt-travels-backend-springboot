package com.godwintech.gttravels.dto;

public class RefreshTokenResult {

    private final String rawToken;
    private final long maxAgeSeconds;

    public RefreshTokenResult(String rawToken, long maxAgeSeconds) {
        this.rawToken = rawToken;
        this.maxAgeSeconds = maxAgeSeconds;
    }

    public String getRawToken() {
        return rawToken;
    }

    public long getMaxAgeSeconds() {
        return maxAgeSeconds;
    }
}
