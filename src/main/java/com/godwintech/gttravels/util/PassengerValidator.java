package com.godwintech.gttravels.util;

import java.util.regex.Pattern;

public final class PassengerValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z .'-]{1,98}[A-Za-z.']?$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private PassengerValidator() {
    }

    public static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Passenger name is required");
        }
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new IllegalArgumentException("Invalid passenger name");
        }
    }

    public static void validateAge(Integer age) {
        if (age == null || age < 1 || age > 120) {
            throw new IllegalArgumentException("Passenger age must be between 1 and 120");
        }
    }

    public static void validateMobile(String mobile) {
        if (mobile == null || mobile.isBlank()) {
            return;
        }
        String normalized = mobile.replaceAll("[^0-9]", "");
        if (normalized.length() > 10) {
            normalized = normalized.substring(normalized.length() - 10);
        }
        if (!MOBILE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid Indian mobile number");
        }
    }

    public static String normalizeMobile(String mobile) {
        if (mobile == null || mobile.isBlank()) {
            return null;
        }
        String normalized = mobile.replaceAll("[^0-9]", "");
        if (normalized.length() > 10) {
            normalized = normalized.substring(normalized.length() - 10);
        }
        return normalized;
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }
}
