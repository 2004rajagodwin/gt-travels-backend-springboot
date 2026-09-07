package com.godwintech.gttravels.util;

import com.godwintech.gttravels.repository.RefundRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class RefundReferenceGenerator {

    private static final String PREFIX = "REF-";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final RefundRepository refundRepository;

    public RefundReferenceGenerator(RefundRepository refundRepository) {
        this.refundRepository = refundRepository;
    }

    public String generate() {
        for (int attempt = 0; attempt < 10; attempt++) {
            String reference = PREFIX + randomSuffix(6);
            if (!refundRepository.existsByRefundReference(reference)) {
                return reference;
            }
        }
        return PREFIX + System.currentTimeMillis();
    }

    private String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
