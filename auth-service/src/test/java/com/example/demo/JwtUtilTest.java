package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import com.example.demo.security.JwtUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generateToken_thenExtractUsername_returnsSameUsername() {
        String token = jwtUtil.generateToken("riya");

        assertNotNull(token);
        assertEquals("riya", jwtUtil.extractUsername(token));
    }

    @Test
    void validateToken_withFreshToken_returnsTrue() {
        String token = jwtUtil.generateToken("riya");

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_withGarbage_returnsFalse() {
        assertFalse(jwtUtil.validateToken("not.a.real.token"));
    }

    @Test
    void validateToken_signedWithDifferentKey_returnsFalse() {
        SecretKey otherKey = Keys.hmacShaKeyFor(
                "SomeCompletelyDifferentSecretKey1234567890".getBytes());
        String forged = Jwts.builder()
                .subject("riya")
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(otherKey)
                .compact();

        assertFalse(jwtUtil.validateToken(forged));
    }

    @Test
    void validateToken_expiredToken_returnsFalse() {
        SecretKey sameKey = Keys.hmacShaKeyFor(
                "ThisIsASecretKeyForJWTGenerationChangeMe123456".getBytes());
        String expired = Jwts.builder()
                .subject("riya")
                .issuedAt(new Date(System.currentTimeMillis() - 120000))
                .expiration(new Date(System.currentTimeMillis() - 60000))
                .signWith(sameKey)
                .compact();

        assertFalse(jwtUtil.validateToken(expired));
    }
}