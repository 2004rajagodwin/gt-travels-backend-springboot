package com.godwintech.gttravels.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dropping_points")
public class DroppingPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(nullable = false)
    private String pointName;

    @Column(nullable = false)
    private String dropTime;

    @Column(nullable = false)
    private Integer sequenceNo;

    private String address;

    private String landmark;

    private String city;

    @Column(nullable = false)
    private boolean active = true;

    public DroppingPoint() {
    }

    public DroppingPoint(Long id,
                         Route route,
                         String pointName,
                         String dropTime,
                         Integer sequenceNo) {
        this.id = id;
        this.route = route;
        this.pointName = pointName;
        this.dropTime = dropTime;
        this.sequenceNo = sequenceNo;
    }

    public Long getId() {
        return id;
    }

    public Route getRoute() {
        return route;
    }

    public String getPointName() {
        return pointName;
    }

    public String getDropTime() {
        return dropTime;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setDropTime(String dropTime) {
        this.dropTime = dropTime;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}