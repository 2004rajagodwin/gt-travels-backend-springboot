package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.CancellationPreviewResponse;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.CancellationPolicy;
import com.godwintech.gttravels.entity.CancellationPolicyRule;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.PolicyScopeType;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.repository.CancellationPolicyRepository;
import com.godwintech.gttravels.util.DiscountCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CancellationService {

    private final CancellationPolicyRepository policyRepository;

    public CancellationService(CancellationPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public CancellationPreviewResponse preview(Booking booking) {
        validateCancellable(booking);
        return buildPreview(booking);
    }

    public CancellationCalculation calculate(Booking booking) {
        validateCancellable(booking);
        CancellationPreviewResponse preview = buildPreview(booking);
        return new CancellationCalculation(
                preview.getOriginalAmount(),
                preview.getCancellationCharge(),
                preview.getRefundAmount(),
                preview.getRefundPercentage(),
                preview.getPolicyName());
    }

    public void validateCancellable(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictException("Booking is already cancelled");
        }
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BadRequestException("Completed bookings cannot be cancelled");
        }
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("Only confirmed bookings can be cancelled");
        }
        LocalDateTime departure = booking.getSchedule().getDepartureTime();
        if (departure != null && departure.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot cancel after journey departure");
        }
    }

    private CancellationPreviewResponse buildPreview(Booking booking) {
        double originalAmount = booking.getTotalAmount() != null ? booking.getTotalAmount() : 0;
        CancellationPolicy policy = resolvePolicy(booking);
        double refundPercentage = resolveRefundPercentage(policy, booking);
        double refundAmount = DiscountCalculator.round(originalAmount * refundPercentage / 100.0);
        double cancellationCharge = DiscountCalculator.round(originalAmount - refundAmount);

        CancellationPreviewResponse response = new CancellationPreviewResponse();
        response.setBookingId(booking.getBookingId());
        response.setPnr(booking.getPnr());
        response.setOriginalAmount(originalAmount);
        response.setCancellationCharge(cancellationCharge);
        response.setRefundAmount(refundAmount);
        response.setRefundPercentage(refundPercentage);
        response.setPolicyName(policy != null ? policy.getName() : "Default Policy");
        if (policy != null) {
            response.setPolicyRules(policy.getRules().stream()
                    .map(r -> new CancellationPreviewResponse.PolicyRuleResponse(
                            r.getMinHoursBeforeJourney(), r.getRefundPercentage()))
                    .toList());
        }
        return response;
    }

    private CancellationPolicy resolvePolicy(Booking booking) {
        List<PolicyScopeType> scopes = List.of(
                PolicyScopeType.SCHEDULE,
                PolicyScopeType.BUS,
                PolicyScopeType.ROUTE,
                PolicyScopeType.OPERATOR,
                PolicyScopeType.GLOBAL);

        for (PolicyScopeType scope : scopes) {
            Long scopeId = resolveScopeId(booking, scope);
            var policy = policyRepository.findActiveByScope(scope, scopeId);
            if (policy.isPresent() && !policy.get().getRules().isEmpty()) {
                return policy.get();
            }
        }

        return policyRepository.findActiveByScope(PolicyScopeType.GLOBAL, null).orElse(null);
    }

    private Long resolveScopeId(Booking booking, PolicyScopeType scope) {
        return switch (scope) {
            case SCHEDULE -> booking.getSchedule().getId();
            case BUS -> booking.getBus().getId();
            case ROUTE -> booking.getSchedule().getRoute().getId();
            case OPERATOR -> booking.getBus().getOperator() != null
                    ? booking.getBus().getOperator().getId() : null;
            case GLOBAL -> null;
        };
    }

    private double resolveRefundPercentage(CancellationPolicy policy, Booking booking) {
        if (policy == null || policy.getRules().isEmpty()) {
            return 0;
        }
        LocalDateTime departure = booking.getSchedule().getDepartureTime();
        long hoursBefore = departure != null
                ? Math.max(0, Duration.between(LocalDateTime.now(), departure).toHours())
                : 0;

        return policy.getRules().stream()
                .filter(r -> hoursBefore >= r.getMinHoursBeforeJourney())
                .map(CancellationPolicyRule::getRefundPercentage)
                .findFirst()
                .orElse(0.0);
    }

    public record CancellationCalculation(
            double originalAmount,
            double cancellationCharge,
            double refundAmount,
            double refundPercentage,
            String policyName) {
    }
}
