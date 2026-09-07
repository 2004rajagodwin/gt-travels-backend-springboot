package com.godwintech.gttravels.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassengerValidatorTest {

    @Test
    void validateName_rejectsInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> PassengerValidator.validateName("John@Doe"));
    }

    @Test
    void validateAge_rejectsInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> PassengerValidator.validateAge(0));
        assertThrows(IllegalArgumentException.class, () -> PassengerValidator.validateAge(200));
    }

    @Test
    void validateMobile_acceptsIndianFormat() {
        assertDoesNotThrow(() -> PassengerValidator.validateMobile("9876543210"));
        assertEquals("9876543210", PassengerValidator.normalizeMobile("+91 9876543210"));
    }

    @Test
    void validateMobile_rejectsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> PassengerValidator.validateMobile("12345"));
    }
}
