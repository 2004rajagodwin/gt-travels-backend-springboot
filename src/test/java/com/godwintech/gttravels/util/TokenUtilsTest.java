package com.godwintech.gttravels.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilsTest {

    @Test
    void generateSecureToken_producesUniqueValues() {
        String token1 = TokenUtils.generateSecureToken();
        String token2 = TokenUtils.generateSecureToken();
        assertNotEquals(token1, token2);
        assertTrue(token1.length() > 20);
    }

    @Test
    void hashToken_isDeterministic() {
        String raw = "sample-token";
        assertEquals(TokenUtils.hashToken(raw), TokenUtils.hashToken(raw));
        assertNotEquals(raw, TokenUtils.hashToken(raw));
    }
}
