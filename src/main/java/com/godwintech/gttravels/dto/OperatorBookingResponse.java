package com.godwintech.gttravels.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OperatorBookingResponse {

    private String bookingId;
    private String passengerName;
    private String passengerPhone;
    private String busNumber;
    private String source;
    private String destination;
    private List<String> seats;
    private Double amount;
    private String status;
    private LocalDateTime bookingTime;

    public OperatorBookingResponse() {
    }

    public OperatorBookingResponse(
            String bookingId,
            String passengerName,
            String passengerPhone,
            String busNumber,
            String source,
            String destination,
            List<String> seats,
            Double amount,
            String status,
            LocalDateTime bookingTime) {

        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
        this.busNumber = busNumber;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.amount = amount;
        this.status = status;
        this.bookingTime = bookingTime;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getPassengerPhone() { return passengerPhone; }
    public void setPassengerPhone(String passengerPhone) { this.passengerPhone = passengerPhone; }

    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public List<String> getSeats() { return seats; }
    public void setSeats(List<String> seats) { this.seats = seats; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }
}