package com.godwintech.gttravels.dto;

public class RouteResponse {

    private Long id;
    private String source;
    private String destination;
    private Integer distance;
    private String duration;

    public RouteResponse() {
    }

    public RouteResponse(
            Long id,
            String source,
            String destination,
            Integer distance,
            String duration) {

        this.id = id;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}