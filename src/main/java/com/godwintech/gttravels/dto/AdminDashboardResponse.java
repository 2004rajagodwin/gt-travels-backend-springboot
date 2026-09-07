package com.godwintech.gttravels.dto;

public class AdminDashboardResponse {

    private Long totalUsers;
    private Long totalCustomers;
    private Long totalOperators;
    private Long totalBuses;
    private Long totalRoutes;
    private Long totalSchedules;
    private Long totalBookings;
    private Double totalRevenue;

    public AdminDashboardResponse() {
    }

    public Long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getTotalOperators() {
        return totalOperators;
    }

    public void setTotalOperators(Long totalOperators) {
        this.totalOperators = totalOperators;
    }

    public Long getTotalBuses() {
        return totalBuses;
    }

    public void setTotalBuses(Long totalBuses) {
        this.totalBuses = totalBuses;
    }

    public Long getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalRoutes(Long totalRoutes) {
        this.totalRoutes = totalRoutes;
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