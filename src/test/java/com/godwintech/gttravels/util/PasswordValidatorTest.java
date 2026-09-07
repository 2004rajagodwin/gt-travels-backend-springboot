package com.godwintech.gttravels.util;

import com.godwintech.gttravels.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Test
    void validate_acceptsStrongPassword() {
        assertDoesNotThrow(() -> PasswordValidator.validate("Password@1"));
    }

    @Test
    void validate_rejectsWeakPassword() {
        assertThrows(BadRequestException.class, () -> PasswordValidator.validate("password"));
    }
}
