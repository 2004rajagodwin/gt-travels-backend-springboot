package com.godwintech.gttravels.dto;

public class OperatorDashboardResponse {

    private Long totalBuses;
    private Long totalSchedules;
    private Long totalBookings;
    private Double totalRevenue;

    public OperatorDashboardResponse() {
    }

    public Long getTotalBuses() {
        return totalBuses;
    }

    public void setTotalBuses(Long totalBuses) {
        this.totalBuses = totalBuses;
    }

    public Long getTotalSchedules() {
        return totalSchedules;
    }

    public void setTotalSchedules(Long totalSchedules) {
        this.totalSchedules = totalSchedules;
    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(Long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}