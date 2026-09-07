package com.godwintech.gttravels.util;

import java.util.regex.Pattern;

public final class RegistrationNumberValidator {

    private static final Pattern INDIAN_REGISTRATION = Pattern.compile(
            "^[A-Z]{2}\\s?\\d{1,2}\\s?[A-Z]{1,3}\\s?\\d{1,4}$",
            Pattern.CASE_INSENSITIVE);

    private RegistrationNumberValidator() {
    }

    public static boolean isValid(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            return false;
        }
        return INDIAN_REGISTRATION.matcher(registrationNumber.trim()).matches();
    }

    public static String normalize(String registrationNumber) {
        return registrationNumber.trim().toUpperCase().replaceAll("\\s+", " ");
    }
}
