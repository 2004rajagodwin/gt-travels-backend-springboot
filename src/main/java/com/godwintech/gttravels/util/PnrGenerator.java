package com.godwintech.gttravels.util;

import com.godwintech.gttravels.repository.BookingRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Year;
import java.util.Locale;

@Component
public class PnrGenerator {

    private static final String ALPHANUM = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final BookingRepository bookingRepository;

    public PnrGenerator(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public String generate() {
        for (int attempt = 0; attempt < 20; attempt++) {
            String pnr = "GTB-" + Year.now().getValue() + "-" + randomSuffix(6);
            if (!bookingRepository.existsByPnr(pnr)) {
                return pnr;
            }
        }
        throw new IllegalStateException("Unable to generate unique PNR");
    }

    private String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RANDOM.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }
}
