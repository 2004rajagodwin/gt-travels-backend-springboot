package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.*;
import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.BookingService;
import com.godwintech.gttravels.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping("/fare-preview")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<FareBreakdownResponse> previewFare(
            @Valid @RequestBody FarePreviewRequest request) {
        return ResponseEntity.ok(bookingService.previewFare(request));
    }

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> initiateBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.initiateBooking(request, user.getEmail()));
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingDetailsResponse> confirmBooking(
            @Valid @RequestBody PaymentOrderRequest request) {
        User user = userService.getCurrentUser();
        Booking booking = bookingService.confirmBooking(request, user);
        return ResponseEntity.ok(bookingService.getBookingById(booking.getBookingId(), user));
    }

    @PostMapping("/{bookingId}/retry-payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingResponse> retryPayment(@PathVariable String bookingId) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.retryPayment(bookingId, user));
    }

    @PostMapping("/{bookingId}/cancellation-preview")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CancellationPreviewResponse> previewCancellation(@PathVariable String bookingId) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.previewCancellation(bookingId, user));
    }

    @PostMapping("/cancel/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CancellationResultResponse> cancelBooking(@PathVariable String bookingId) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, user));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingSummaryResponse>> getMyBookings(
            @RequestParam(required = false) String tab) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.getMyBookings(user.getEmail(), tab));
    }

    @GetMapping("/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingDetailsResponse> getBooking(@PathVariable String bookingId) {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(bookingService.getBookingById(bookingId, user));
    }

    @GetMapping("/admin/bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<BookingSummaryResponse>> searchAdminBookings(
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                bookingService.searchAdminBookings(pnr, reference, status, date, page, size));
    }

    @GetMapping("/admin/bookings/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingSummaryResponse>> filterBookings(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String bus) {
        return ResponseEntity.ok(bookingService.filterBookings(date, bus));
    }

    @GetMapping("/operator/bookings")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<PageResponse<BookingSummaryResponse>> searchOperatorBookings(
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Long scheduleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
                bookingService.searchOperatorBookings(pnr, date, scheduleId, page, size));
    }

    @GetMapping("/operator/bookings/filter")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<BookingSummaryResponse>> filterOperatorBookings(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String bus) {
        return ResponseEntity.ok(bookingService.filterOperatorBookings(date, bus));
    }
}
