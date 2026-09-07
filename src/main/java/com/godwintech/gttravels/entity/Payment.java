package com.godwintech.gttravels.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private double amount;
    private String currency;
    private String status;
    private LocalDateTime paymentTime;
}