package com.godwintech.gttravels.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.booking")
public class BookingProperties {

    private int seatLockMinutes = 10;
    private int pendingExpiryMinutes = 15;

    public int getSeatLockMinutes() {
        return seatLockMinutes;
    }

    public void setSeatLockMinutes(int seatLockMinutes) {
        this.seatLockMinutes = seatLockMinutes;
    }

    public int getPendingExpiryMinutes() {
        return pendingExpiryMinutes;
    }

    public void setPendingExpiryMinutes(int pendingExpiryMinutes) {
        this.pendingExpiryMinutes = pendingExpiryMinutes;
    }
}
