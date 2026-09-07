package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Booking;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final TicketService ticketService;

    public MailService(JavaMailSender mailSender,
                       TicketService ticketService) {
        this.mailSender = mailSender;
        this.ticketService = ticketService;
    }

    public void sendBookingTicket(Booking booking) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(booking.getPassenger().getEmail());

            helper.setSubject("GT Travels - Booking Confirmed");

            String html = """
                    <html>
                    <body style='font-family:Arial,sans-serif;'>

                    <h2 style='color:#0d6efd;'>
                    🎉 Booking Confirmed
                    </h2>

                    <p>Dear <b>%s</b>,</p>

                    <p>Your booking has been confirmed successfully.</p>

                    <table border='1'
                           cellpadding='8'
                           cellspacing='0'
                           style='border-collapse:collapse;width:100%%;'>

                        <tr>
                            <th align='left'>Booking ID</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Bus</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Route</th>
                            <td>%s → %s</td>
                        </tr>

                        <tr>
                            <th align='left'>Boarding Point</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Dropping Point</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Departure</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Seats</th>
                            <td>%s</td>
                        </tr>

                        <tr>
                            <th align='left'>Amount</th>
                            <td>₹ %.2f</td>
                        </tr>

                    </table>

                    <br>

                    <p>Please find your e-ticket attached with this email.</p>

                    <br>

                    <p>
                    Thank you for choosing <b>GT Travels</b>.<br><br>

                    Regards,<br>
                    <b>GT Travels Team</b>
                    </p>

                    </body>
                    </html>
                    """.formatted(

                    booking.getPassenger().getName(),

                    booking.getBookingId(),

                    booking.getBus().getBusNumber(),

                    booking.getSchedule().getRoute().getSource(),
                    booking.getSchedule().getRoute().getDestination(),

                    booking.getBoardingPoint(),

                    booking.getDropPoint(),

                    booking.getSchedule().getDepartureTime(),

                    booking.getSeats()
                            .stream()
                            .map(seat -> seat.getSeatNumber())
                            .toList(),

                    booking.getTotalAmount()
            );

            helper.setText(html, true);

            byte[] pdf = ticketService.generateTicketPdf(booking);

            helper.addAttachment(
                    "GT-Ticket-" + booking.getBookingId() + ".pdf",
                    new ByteArrayResource(pdf)
            );

            mailSender.send(message);
            log.info("Booking confirmation email sent for booking {}", booking.getBookingId());

        } catch (Exception e) {

            log.error("Failed to send booking email for booking {}", booking.getBookingId(), e);
            throw new RuntimeException("Mail send failed");
        }
    }
}