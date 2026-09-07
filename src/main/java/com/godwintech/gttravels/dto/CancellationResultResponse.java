package com.godwintech.gttravels.dto;

public class CancellationResultResponse {

    private String bookingId;
    private String pnr;
    private String status;
    private String paymentStatus;
    private double cancellationCharge;
    private double refundAmount;
    private RefundResponse refund;

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public double getCancellationCharge() { return cancellationCharge; }
    public void setCancellationCharge(double cancellationCharge) { this.cancellationCharge = cancellationCharge; }
    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }
    public RefundResponse getRefund() { return refund; }
    public void setRefund(RefundResponse refund) { this.refund = refund; }
}
