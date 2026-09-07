package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.IndianBusType;
import com.godwintech.gttravels.enums.SeatConfigurationType;

import java.time.LocalDate;
import java.util.List;

public class BusSearchFilterRequest {

    private String source;
    private String destination;
    private String from;
    private String to;
    private LocalDate date;
    private LocalDate travelDate;

    private IndianBusType busType;
    private String ac;
    private SeatConfigurationType seatType;
    private Double minPrice;
    private Double maxPrice;
    private String departureFrom;
    private String departureTo;
    private Double rating;
    private Long operator;
    private List<Long> amenities;

    private int page = 0;
    private int size = 10;
    private String sort = "recommended";

    public BusSearchFilterRequest() {
    }

    public String getSource() {
        return source != null ? source : from;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination != null ? destination : to;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getDate() {
        return date != null ? date : travelDate;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public IndianBusType getBusType() {
        return busType;
    }

    public void setBusType(IndianBusType busType) {
        this.busType = busType;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public SeatConfigurationType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatConfigurationType seatType) {
        this.seatType = seatType;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getDepartureFrom() {
        return departureFrom;
    }

    public void setDepartureFrom(String departureFrom) {
        this.departureFrom = departureFrom;
    }

    public String getDepartureTo() {
        return departureTo;
    }

    public void setDepartureTo(String departureTo) {
        this.departureTo = departureTo;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public List<Long> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Long> amenities) {
        this.amenities = amenities;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
