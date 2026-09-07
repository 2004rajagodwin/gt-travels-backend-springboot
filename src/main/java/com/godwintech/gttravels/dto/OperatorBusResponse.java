package com.godwintech.gttravels.dto;

public class OperatorBusResponse {

    private Long id;
    private String busNumber;
    private String busType;
    private Integer totalSeats;

    public OperatorBusResponse() {
    }

    public OperatorBusResponse(Long id,
                               String busNumber,
                               String busType,
                               Integer totalSeats) {

        this.id = id;
        this.busNumber = busNumber;
        this.busType = busType;
        this.totalSeats = totalSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
}