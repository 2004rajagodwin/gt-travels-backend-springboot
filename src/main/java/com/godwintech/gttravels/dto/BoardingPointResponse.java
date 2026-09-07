package com.godwintech.gttravels.dto;

public class BoardingPointResponse {

    private Long id;

    private Long routeId;

    private String routeName;

    private String pointName;

    private String pickupTime;

    private Integer sequenceNo;

    private String address;
    private String landmark;
    private String city;

    public BoardingPointResponse() {
    }

    public BoardingPointResponse(
            Long id,
            Long routeId,
            String routeName,
            String pointName,
            String pickupTime,
            Integer sequenceNo) {
        this(id, routeId, routeName, pointName, pickupTime, sequenceNo, null, null, null);
    }

    public BoardingPointResponse(
            Long id,
            Long routeId,
            String routeName,
            String pointName,
            String pickupTime,
            Integer sequenceNo,
            String address,
            String landmark,
            String city) {

        this.id = id;
        this.routeId = routeId;
        this.routeName = routeName;
        this.pointName = pointName;
        this.pickupTime = pickupTime;
        this.sequenceNo = sequenceNo;
        this.address = address;
        this.landmark = landmark;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public String getPointName() {
        return pointName;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}