package com.godwintech.gttravels.dto;

public class BusImageResponse {

    private Long id;
    private String imageUrl;
    private boolean primaryImage;

    public BusImageResponse() {
    }

    public BusImageResponse(Long id, String imageUrl, boolean primaryImage) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(boolean primaryImage) {
        this.primaryImage = primaryImage;
    }
}
