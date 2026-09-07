package com.godwintech.gttravels.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Value("${razorpay.currency:INR}")
    private String defaultCurrency;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() {
        try {
            razorpayClient = new RazorpayClient(keyId, keySecret);
            log.info("Razorpay client initialized");
        } catch (Exception e) {
            log.error("Failed to initialize Razorpay client", e);
        }
    }

    public Order createOrder(double amount) {
        return createOrder(amount, defaultCurrency);
    }

    public Order createOrder(double amount, String currency) {
        try {
            JSONObject options = new JSONObject();
            options.put("amount", (int) (amount * 100));
            options.put("currency", currency);
            options.put("payment_capture", 1);
            return razorpayClient.orders.create(options);
        } catch (Exception e) {
            log.error("Failed to create Razorpay order", e);
            throw new RuntimeException("Failed to create payment order");
        }
    }

    public boolean verifyPaymentSignature(
            String orderId,
            String paymentId,
            String signature) {

        try {
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", orderId);
            attributes.put("razorpay_payment_id", paymentId);
            attributes.put("razorpay_signature", signature);
            Utils.verifyPaymentSignature(attributes, keySecret);
            return true;
        } catch (RazorpayException e) {
            log.warn("Payment signature verification failed for order {}", orderId);
            return false;
        }
    }

    public String getKeyId() {
        return keyId;
    }

    public String refundPayment(String paymentId, double amount) {
        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", (int) Math.round(amount * 100));
            com.razorpay.Refund refund = razorpayClient.payments.refund(paymentId, refundRequest);
            return refund.get("id");
        } catch (Exception e) {
            log.error("Failed to process Razorpay refund for payment {}", paymentId, e);
            throw new RuntimeException("Failed to process refund");
        }
    }
}
