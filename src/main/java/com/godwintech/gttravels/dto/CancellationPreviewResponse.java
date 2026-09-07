package com.godwintech.gttravels.dto;

import java.util.List;

public class CancellationPreviewResponse {

    private String bookingId;
    private String pnr;
    private double originalAmount;
    private double cancellationCharge;
    private double refundAmount;
    private double refundPercentage;
    private String policyName;
    private List<PolicyRuleResponse> policyRules;

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }
    public double getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(double originalAmount) { this.originalAmount = originalAmount; }
    public double getCancellationCharge() { return cancellationCharge; }
    public void setCancellationCharge(double cancellationCharge) { this.cancellationCharge = cancellationCharge; }
    public double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(double refundAmount) { this.refundAmount = refundAmount; }
    public double getRefundPercentage() { return refundPercentage; }
    public void setRefundPercentage(double refundPercentage) { this.refundPercentage = refundPercentage; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public List<PolicyRuleResponse> getPolicyRules() { return policyRules; }
    public void setPolicyRules(List<PolicyRuleResponse> policyRules) { this.policyRules = policyRules; }

    public static class PolicyRuleResponse {
        private int minHoursBeforeJourney;
        private double refundPercentage;

        public PolicyRuleResponse() {}
        public PolicyRuleResponse(int minHoursBeforeJourney, double refundPercentage) {
            this.minHoursBeforeJourney = minHoursBeforeJourney;
            this.refundPercentage = refundPercentage;
        }
        public int getMinHoursBeforeJourney() { return minHoursBeforeJourney; }
        public void setMinHoursBeforeJourney(int minHoursBeforeJourney) { this.minHoursBeforeJourney = minHoursBeforeJourney; }
        public double getRefundPercentage() { return refundPercentage; }
        public void setRefundPercentage(double refundPercentage) { this.refundPercentage = refundPercentage; }
    }
}
