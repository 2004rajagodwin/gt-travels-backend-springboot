package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.RefundResponse;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.Refund;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AuditActionType;
import com.godwintech.gttravels.enums.PaymentStatus;
import com.godwintech.gttravels.enums.RefundStatus;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.exception.ForbiddenException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.RefundRepository;
import com.godwintech.gttravels.util.RefundReferenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundService.class);
    private static final List<RefundStatus> ACTIVE_REFUND_STATUSES =
            List.of(RefundStatus.PENDING, RefundStatus.PROCESSING, RefundStatus.COMPLETED);

    private final RefundRepository refundRepository;
    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final RefundReferenceGenerator refundReferenceGenerator;
    private final AuditLogService auditLogService;

    public RefundService(
            RefundRepository refundRepository,
            BookingRepository bookingRepository,
            PaymentService paymentService,
            RefundReferenceGenerator refundReferenceGenerator,
            AuditLogService auditLogService) {
        this.refundRepository = refundRepository;
        this.bookingRepository = bookingRepository;
        this.paymentService = paymentService;
        this.refundReferenceGenerator = refundReferenceGenerator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public RefundResponse processCancellationRefund(Booking booking, User user, String reason) {
        verifyCustomerOwnership(booking, user);

        Refund existing = refundRepository.findByBooking_BookingId(booking.getBookingId()).orElse(null);
        if (existing != null && ACTIVE_REFUND_STATUSES.contains(existing.getStatus())) {
            return toResponse(existing);
        }

        if (booking.getPaymentStatus() != PaymentStatus.PAID
                && booking.getPaymentStatus() != PaymentStatus.REFUND_PENDING) {
            throw new BadRequestException("Booking payment is not refundable");
        }
        if (booking.getRazorpayPaymentId() == null || booking.getRazorpayPaymentId().isBlank()) {
            throw new BadRequestException("No payment found for refund");
        }

        double refundAmount = booking.getRefundAmount() != null ? booking.getRefundAmount() : 0;
        if (refundAmount <= 0) {
            throw new BadRequestException("No refund available for this cancellation");
        }

        Refund refund = new Refund();
        refund.setBooking(booking);
        refund.setRefundReference(refundReferenceGenerator.generate());
        refund.setAmount(refundAmount);
        refund.setReason(reason != null ? reason : "Customer cancellation");
        refund.setStatus(RefundStatus.PENDING);
        refund.setProviderPaymentId(booking.getRazorpayPaymentId());
        refund = refundRepository.save(refund);

        auditLogService.log(AuditActionType.REFUND_CREATED, "Refund",
                refund.getRefundReference(), user, "Refund initiated for booking " + booking.getPnr());

        return processRefund(refund, user);
    }

    @Transactional
    protected RefundResponse processRefund(Refund refund, User actor) {
        Booking booking = refund.getBooking();
        refund.setStatus(RefundStatus.PROCESSING);
        refundRepository.save(refund);

        try {
            String providerRefundId = paymentService.refundPayment(
                    refund.getProviderPaymentId(), refund.getAmount());
            refund.setProviderRefundId(providerRefundId);
            refund.setStatus(RefundStatus.COMPLETED);
            refund.setProcessedAt(LocalDateTime.now());
            booking.setPaymentStatus(PaymentStatus.REFUNDED);
            auditLogService.log(AuditActionType.REFUND_PROCESSED, "Refund",
                    refund.getRefundReference(), actor, "Refund completed");
        } catch (Exception e) {
            log.error("Refund failed for {}", refund.getRefundReference(), e);
            refund.setStatus(RefundStatus.FAILED);
            refund.setFailureReason("Refund could not be processed. Please contact support.");
            booking.setPaymentStatus(PaymentStatus.REFUND_FAILED);
            auditLogService.log(AuditActionType.REFUND_FAILED, "Refund",
                    refund.getRefundReference(), actor, "Refund failed");
        }

        refundRepository.save(refund);
        bookingRepository.save(booking);
        return toResponse(refund);
    }

    public RefundResponse getRefundForBooking(String bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        verifyCustomerOwnership(booking, user);
        Refund refund = refundRepository.findByBooking_BookingIdOrderByCreatedAtDesc(bookingId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Refund not found"));
        return toResponse(refund);
    }

    public java.util.Optional<RefundResponse> findLatestForBooking(String bookingId) {
        return refundRepository.findByBooking_BookingIdOrderByCreatedAtDesc(bookingId).stream()
                .findFirst()
                .map(this::toResponse);
    }

    public PageResponse<RefundResponse> searchAdminRefunds(String pnr, String reference, String status, int page, int size) {
        Page<Refund> result = refundRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (reference != null && !reference.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("refundReference")),
                        "%" + reference.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), RefundStatus.valueOf(status)));
            }
            if (pnr != null && !pnr.isBlank()) {
                predicates.add(cb.like(cb.lower(root.join("booking").get("pnr")),
                        "%" + pnr.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    private void verifyCustomerOwnership(Booking booking, User user) {
        if (!booking.getPassenger().getId().equals(user.getId())) {
            throw new ForbiddenException("You do not have access to this booking");
        }
    }

    public RefundResponse toResponse(Refund refund) {
        RefundResponse r = new RefundResponse();
        r.setId(refund.getId());
        r.setBookingId(refund.getBooking().getBookingId());
        r.setPnr(refund.getBooking().getPnr());
        r.setRefundReference(refund.getRefundReference());
        r.setAmount(refund.getAmount());
        r.setReason(refund.getReason());
        r.setStatus(refund.getStatus());
        r.setProvider(refund.getProvider());
        r.setProviderRefundId(refund.getProviderRefundId());
        r.setFailureReason(refund.getFailureReason());
        r.setCreatedAt(refund.getCreatedAt());
        r.setProcessedAt(refund.getProcessedAt());
        return r;
    }
}
