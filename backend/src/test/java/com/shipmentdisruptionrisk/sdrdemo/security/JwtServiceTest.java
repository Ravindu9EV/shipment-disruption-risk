package com.shipmentdisruptionrisk.sdrdemo.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = JwtService.forTesting(
            "test-secret-at-least-32-characters-long-for-hmac",
            3600000
        );
    }

    @Test
    void generateToken_shouldContainUsernameAndRole() {
        String username = "abc";
        String role = "USER";
        String token = jwtService.generateToken(username, role);
        assertEquals(username, jwtService.extractUsername(token));
        assertEquals(role, jwtService.extractRole(token));
    }

    @Test
    void isTokenValid_shouldReturnTrueForFreshToken() {
        String token = jwtService.generateToken("abc", "USER");
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_shouldReturnFalseForTamperedToken() {
        String token = jwtService.generateToken("abc", "USER");
        char last = token.charAt(token.length() - 1);
        char replacement = (last == 'a') ? 'b' : 'a';
        String tampered = token.substring(0, token.length() - 1) + replacement;
        assertFalse(jwtService.isTokenValid(tampered));
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() {
        JwtService expiredService = JwtService.forTesting(
            "test-secret-at-least-32-characters-long-for-hmac",
            -1000
        );
        String token = expiredService.generateToken("abc", "USER");
        assertFalse(expiredService.isTokenValid(token));
    }

    @Test
    void extractUsername_shouldThrowOnMalformedToken() {
        String malformed = "not.a.jwt";
        assertThrows(JwtException.class, () -> jwtService.extractUsername(malformed));
    }
}
