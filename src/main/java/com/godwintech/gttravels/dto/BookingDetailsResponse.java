package com.godwintech.gttravels.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDetailsResponse {

    private String bookingId;
    private String pnr;
    private String bookingReference;
    private String passengerName;
    private String passengerEmail;
    private String busNumber;
    private String busName;
    private String busType;
    private String operatorName;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private LocalDate journeyDate;
    private List<String> seats;
    private List<PassengerResponse> passengers;
    private Double baseFare;
    private Double boardingCharges;
    private Double convenienceFee;
    private Double tax;
    private Double discount;
    private Double totalAmount;
    private String status;
    private String paymentStatus;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private LocalDateTime bookingTime;
    private String boardingPoint;
    private String dropPoint;
    private String couponCode;
    private Double couponDiscount;
    private Double offerDiscount;
    private Double cancellationCharge;
    private Double refundAmount;
    private RefundResponse refund;
    private CancellationPreviewResponse cancellationPolicy;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats;
    }

    public List<PassengerResponse> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerResponse> passengers) {
        this.passengers = passengers;
    }

    public Double getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(Double baseFare) {
        this.baseFare = baseFare;
    }

    public Double getBoardingCharges() {
        return boardingCharges;
    }

    public void setBoardingCharges(Double boardingCharges) {
        this.boardingCharges = boardingCharges;
    }

    public Double getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(Double convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBoardingPoint() {
        return boardingPoint;
    }

    public void setBoardingPoint(String boardingPoint) {
        this.boardingPoint = boardingPoint;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
    }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public Double getCouponDiscount() { return couponDiscount; }
    public void setCouponDiscount(Double couponDiscount) { this.couponDiscount = couponDiscount; }
    public Double getOfferDiscount() { return offerDiscount; }
    public void setOfferDiscount(Double offerDiscount) { this.offerDiscount = offerDiscount; }
    public Double getCancellationCharge() { return cancellationCharge; }
    public void setCancellationCharge(Double cancellationCharge) { this.cancellationCharge = cancellationCharge; }
    public Double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(Double refundAmount) { this.refundAmount = refundAmount; }
    public RefundResponse getRefund() { return refund; }
    public void setRefund(RefundResponse refund) { this.refund = refund; }
    public CancellationPreviewResponse getCancellationPolicy() { return cancellationPolicy; }
    public void setCancellationPolicy(CancellationPreviewResponse cancellationPolicy) { this.cancellationPolicy = cancellationPolicy; }
}
