package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.PageResponse;
import com.godwintech.gttravels.dto.PaymentSummaryResponse;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.enums.PaymentStatus;
import com.godwintech.gttravels.repository.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminPaymentService {

    private static final List<PaymentStatus> PAYMENT_STATUSES = List.of(
            PaymentStatus.PAID, PaymentStatus.REFUND_PENDING,
            PaymentStatus.REFUNDED, PaymentStatus.REFUND_FAILED);

    private final BookingRepository bookingRepository;

    public AdminPaymentService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public PageResponse<PaymentSummaryResponse> search(String pnr, String status, String date, int page, int size) {
        Page<Booking> result = bookingRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(root.get("paymentStatus").in(PAYMENT_STATUSES));
            if (pnr != null && !pnr.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("pnr")), "%" + pnr.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("paymentStatus"), PaymentStatus.valueOf(status)));
            }
            if (date != null && !date.isBlank()) {
                LocalDate d = LocalDate.parse(date);
                predicates.add(cb.equal(root.get("createdAt").as(LocalDate.class), d));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by(Sort.Direction.DESC, "createdAt")));

        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    private PaymentSummaryResponse toResponse(Booking booking) {
        PaymentSummaryResponse r = new PaymentSummaryResponse();
        r.setPaymentId(booking.getRazorpayPaymentId());
        r.setBookingId(booking.getBookingId());
        r.setPnr(booking.getPnr());
        r.setCustomerName(booking.getPassenger().getName());
        r.setCustomerEmail(booking.getPassenger().getEmail());
        r.setAmount(booking.getTotalAmount() != null ? booking.getTotalAmount() : 0);
        r.setPaymentStatus(booking.getPaymentStatus() != null ? booking.getPaymentStatus().name() : null);
        r.setProvider("RAZORPAY");
        r.setCreatedAt(booking.getCreatedAt());
        return r;
    }
}
