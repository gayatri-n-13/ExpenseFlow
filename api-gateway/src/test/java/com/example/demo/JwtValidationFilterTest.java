package com.example.demo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.example.demo.security.JwtValidationFilter;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class JwtValidationFilterTest {

    private final JwtValidationFilter filter = new JwtValidationFilter();

    // Same secret as the filter and Auth Service
    private final SecretKey key = Keys.hmacShaKeyFor(
            "ThisIsASecretKeyForJWTGenerationChangeMe123456".getBytes());

    @Test
    void request_withoutToken_returns401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/categories");
        request.setRequestURI("/categories");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
    }

    @Test
    void request_withValidToken_passesThrough() throws Exception {
        String token = Jwts.builder()
                .subject("riya")
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(key)
                .compact();

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/categories");
        request.setRequestURI("/categories");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(200, response.getStatus());
    }

    @Test
    void authEndpoints_areOpenWithoutToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/auth/login");
        request.setRequestURI("/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(200, response.getStatus());
    }

    @Test
    void request_withGarbageToken_returns401() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/expenses");
        request.setRequestURI("/expenses");
        request.addHeader("Authorization", "Bearer not.a.real.token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
    }
}