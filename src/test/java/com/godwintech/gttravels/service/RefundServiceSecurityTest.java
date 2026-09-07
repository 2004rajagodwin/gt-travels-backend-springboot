package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.PaymentStatus;
import com.godwintech.gttravels.exception.ForbiddenException;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.RefundRepository;
import com.godwintech.gttravels.util.RefundReferenceGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefundServiceSecurityTest {

    @Mock private RefundRepository refundRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private PaymentService paymentService;
    @Mock private RefundReferenceGenerator refundReferenceGenerator;
    @Mock private AuditLogService auditLogService;

    @InjectMocks
    private RefundService refundService;

    @Test
    void getRefundForBooking_rejectsOtherUser() {
        User owner = new User();
        owner.setId(1L);
        User other = new User();
        other.setId(2L);

        Booking booking = new Booking();
        booking.setBookingId("b1");
        booking.setPassenger(owner);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setRazorpayPaymentId("pay_123");

        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));

        assertThrows(ForbiddenException.class,
                () -> refundService.getRefundForBooking("b1", other));
    }
}
