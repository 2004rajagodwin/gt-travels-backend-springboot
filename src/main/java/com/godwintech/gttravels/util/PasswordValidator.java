package com.godwintech.gttravels.util;

import com.godwintech.gttravels.exception.BadRequestException;

public final class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^()_\\-+=\\[\\]{}:;\"'\\\\|,.<>/?]).{8,}$";

    private PasswordValidator() {
    }

    public static void validate(String password) {
        if (password == null || !password.matches(PASSWORD_PATTERN)) {
            throw new BadRequestException(
                    "Password must be at least 8 characters and include uppercase, lowercase, number, and special character");
        }
    }
}
