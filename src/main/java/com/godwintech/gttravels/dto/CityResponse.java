package com.godwintech.gttravels.dto;

public class CityResponse {

    private Long id;
    private String name;
    private String state;
    private String code;
    private boolean active;

    public CityResponse() {
    }

    public CityResponse(Long id, String name, String state, String code, boolean active) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.code = code;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
