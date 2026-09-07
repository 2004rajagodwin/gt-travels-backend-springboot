package com.godwintech.gttravels.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.fare")
public class FareProperties {

    private double convenienceFeePerSeat = 30.0;
    private double taxPercent = 5.0;
    private double defaultBoardingCharge = 20.0;

    public double getConvenienceFeePerSeat() {
        return convenienceFeePerSeat;
    }

    public void setConvenienceFeePerSeat(double convenienceFeePerSeat) {
        this.convenienceFeePerSeat = convenienceFeePerSeat;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public double getDefaultBoardingCharge() {
        return defaultBoardingCharge;
    }

    public void setDefaultBoardingCharge(double defaultBoardingCharge) {
        this.defaultBoardingCharge = defaultBoardingCharge;
    }
}
