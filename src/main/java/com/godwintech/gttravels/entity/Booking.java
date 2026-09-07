package com.godwintech.gttravels.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @Column(name = "booking_id")
    private String bookingId;

    @Column(unique = true, length = 20)
    private String pnr;

    @Column(name = "booking_reference", unique = true, length = 15)
    private String bookingReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User passenger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Schedule schedule;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "booking_seats",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    @JsonIgnoreProperties({"bus"})
    private List<Seat> seats = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"booking"})
    private List<BookingPassenger> passengers = new ArrayList<>();

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "base_fare")
    private Double baseFare;

    @Column(name = "boarding_charges", nullable = false)
    private Double boardingCharges = 0.0;

    @Column(name = "convenience_fee", nullable = false)
    private Double convenienceFee = 0.0;

    @Column(nullable = false)
    private Double tax = 0.0;

    @Column(nullable = false)
    private Double discount = 0.0;

    @Column(name = "coupon_discount", nullable = false)
    private Double couponDiscount = 0.0;

    @Column(name = "offer_discount", nullable = false)
    private Double offerDiscount = 0.0;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;

    @Column(name = "boarding_point")
    private String boardingPoint;

    @Column(name = "drop_point")
    private String dropPoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boarding_point_id")
    private BoardingPoint boardingPointEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dropping_point_id")
    private DroppingPoint droppingPointEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private PromotionalOffer offer;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_charge")
    private Double cancellationCharge;

    @Column(name = "refund_amount")
    private Double refundAmount;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Booking() {
    }

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

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public List<BookingPassenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<BookingPassenger> passengers) {
        this.passengers = passengers;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Double getOfferDiscount() {
        return offerDiscount;
    }

    public void setOfferDiscount(Double offerDiscount) {
        this.offerDiscount = offerDiscount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public BoardingPoint getBoardingPointEntity() {
        return boardingPointEntity;
    }

    public void setBoardingPointEntity(BoardingPoint boardingPointEntity) {
        this.boardingPointEntity = boardingPointEntity;
    }

    public DroppingPoint getDroppingPointEntity() {
        return droppingPointEntity;
    }

    public void setDroppingPointEntity(DroppingPoint droppingPointEntity) {
        this.droppingPointEntity = droppingPointEntity;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public PromotionalOffer getOffer() {
        return offer;
    }

    public void setOffer(PromotionalOffer offer) {
        this.offer = offer;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Double getCancellationCharge() {
        return cancellationCharge;
    }

    public void setCancellationCharge(Double cancellationCharge) {
        this.cancellationCharge = cancellationCharge;
    }

    public Double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
    }
}
