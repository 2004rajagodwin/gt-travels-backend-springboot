package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Booking;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class TicketService {

    public byte[] generateTicketPdf(Booking booking) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a");

        // Header
        Paragraph title = new Paragraph("GT TRAVELS")
                .setFontSize(24)
                .setBold()
                .setFontColor(ColorConstants.BLUE)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph subTitle = new Paragraph("E - TICKET")
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);
        document.add(subTitle);

        document.add(new Paragraph(" "));

        Paragraph line = new Paragraph(
                "------------------------------------------------------------");
        line.setTextAlignment(TextAlignment.CENTER);

        document.add(line);

        document.add(new Paragraph("PNR : " + (booking.getPnr() != null ? booking.getPnr() : "Pending")));
        document.add(new Paragraph("Booking Ref : " + booking.getBookingReference()));
        document.add(new Paragraph("Booking ID : " + booking.getBookingId()));
        document.add(new Paragraph("Booked By : " + booking.getPassenger().getName()));
        document.add(new Paragraph("Email : " + booking.getPassenger().getEmail()));
        document.add(new Paragraph("Phone : " + booking.getPassenger().getPhone()));

        document.add(new Paragraph(" "));

        document.add(new Paragraph("Bus Number : " + booking.getBus().getBusNumber()));
        document.add(new Paragraph("Bus Type : " + booking.getBus().getBusType()));

        document.add(new Paragraph("From : "
                + booking.getSchedule().getRoute().getSource()));

        document.add(new Paragraph("To : "
                + booking.getSchedule().getRoute().getDestination()));

        document.add(new Paragraph("Distance : "
                + booking.getSchedule().getRoute().getDistance() + " KM"));

        document.add(new Paragraph("Duration : "
                + booking.getSchedule().getRoute().getDuration()));

        document.add(new Paragraph("Departure : "
                + booking.getSchedule().getDepartureTime().format(formatter)));

        document.add(new Paragraph("Arrival : "
                + booking.getSchedule().getArrivalTime().format(formatter)));

        document.add(new Paragraph(" "));

        String seats = booking.getSeats()
                .stream()
                .map(s -> s.getSeatNumber())
                .collect(Collectors.joining(", "));

        document.add(new Paragraph("Seats : " + seats));

        if (booking.getPassengers() != null && !booking.getPassengers().isEmpty()) {
            document.add(new Paragraph("Passengers :"));
            for (var passenger : booking.getPassengers()) {
                document.add(new Paragraph("  - " + passenger.getName()
                        + " (Seat " + passenger.getSeat().getSeatNumber()
                        + ", Age " + passenger.getAge() + ")"));
            }
        }

        document.add(new Paragraph("Base Fare : ₹" + (booking.getBaseFare() != null ? booking.getBaseFare() : 0)));
        document.add(new Paragraph("Boarding Charges : ₹" + booking.getBoardingCharges()));
        document.add(new Paragraph("Convenience Fee : ₹" + booking.getConvenienceFee()));
        document.add(new Paragraph("Tax : ₹" + booking.getTax()));
        if (booking.getDiscount() != null && booking.getDiscount() > 0) {
            document.add(new Paragraph("Discount : -₹" + booking.getDiscount()));
        }
        document.add(new Paragraph("Total Amount : ₹" + booking.getTotalAmount()));

        document.add(new Paragraph("Status : " + booking.getStatus()));
        document.add(new Paragraph("Payment Status : " + booking.getPaymentStatus()));

        document.add(new Paragraph("Payment ID : "
                + booking.getRazorpayPaymentId()));

        if (booking.getBoardingPoint() != null) {
            document.add(new Paragraph("Boarding : "
                    + booking.getBoardingPoint()));
        }

        if (booking.getDropPoint() != null) {
            document.add(new Paragraph("Drop : "
                    + booking.getDropPoint()));
        }

        document.add(new Paragraph(" "));

        // QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(
                booking.getPnr() != null ? booking.getPnr() : booking.getBookingReference(),
                BarcodeFormat.QR_CODE,
                170,
                170);

        ByteArrayOutputStream qr = new ByteArrayOutputStream();

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qr);

        Image qrImage =
                new Image(ImageDataFactory.create(qr.toByteArray()));

        qrImage.setHorizontalAlignment(
                com.itextpdf.layout.properties.HorizontalAlignment.CENTER);

        document.add(qrImage);

        document.add(new Paragraph("Scan to Verify Ticket")
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph(" "));

        Paragraph footer = new Paragraph(
                "Thank you for choosing GT Travels.\nHave a Safe Journey!")
                .setBold()
                .setFontColor(ColorConstants.BLUE)
                .setTextAlignment(TextAlignment.CENTER);

        footer.setBorderTop(new SolidBorder(1));

        document.add(footer);

        document.close();

        return out.toByteArray();
    }
}