package com.godwintech.gttravels.util;

import com.godwintech.gttravels.repository.BookingRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BookingReferenceGenerator {

    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final BookingRepository bookingRepository;

    public BookingReferenceGenerator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String generate() {
        for (int attempt = 0; attempt < 20; attempt++) {
            String ref = "GT-" + randomSuffix(6);
            if (!bookingRepository.existsByBookingReference(ref)) {
                return ref;
            }
        }
        throw new IllegalStateException("Unable to generate unique booking reference");
    }

    private String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
