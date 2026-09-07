package com.godwintech.gttravels.dto;

import java.time.LocalDate;

public class BusSearchRequest {

    private String source;
    private String destination;

    private LocalDate travelDate;

    public BusSearchRequest() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }
}