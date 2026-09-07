package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.entity.Booking;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.service.BookingService;
import com.godwintech.gttravels.service.TicketService;
import com.godwintech.gttravels.service.UserService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final BookingService bookingService;
    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(
            BookingService bookingService,
            TicketService ticketService,
            UserService userService) {

        this.bookingService = bookingService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/download/{bookingId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable String bookingId) {

        try {
            User user = userService.getCurrentUser();
            Booking booking = bookingService.getBookingEntityForCustomer(bookingId, user);

            byte[] pdf = ticketService.generateTicketPdf(booking);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("GT-Ticket-" + bookingId + ".pdf")
                            .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
