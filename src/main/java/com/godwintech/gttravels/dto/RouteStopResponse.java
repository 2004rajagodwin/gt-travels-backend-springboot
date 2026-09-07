package com.godwintech.gttravels.dto;

public class RouteStopResponse {

    private Long id;
    private String cityName;
    private String stopName;
    private Integer sequenceNo;
    private String arrivalTime;
    private String departureTime;
    private Integer distanceKm;

    public RouteStopResponse() {
    }

    public RouteStopResponse(Long id, String cityName, String stopName, Integer sequenceNo,
                             String arrivalTime, String departureTime, Integer distanceKm) {
        this.id = id;
        this.cityName = cityName;
        this.stopName = stopName;
        this.sequenceNo = sequenceNo;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.distanceKm = distanceKm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Integer distanceKm) {
        this.distanceKm = distanceKm;
    }
}
