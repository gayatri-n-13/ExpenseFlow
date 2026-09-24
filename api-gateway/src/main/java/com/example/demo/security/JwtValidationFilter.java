package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    // MUST match the exact same secret key used in Auth Service's JwtUtil
    private final SecretKey secretKey = Keys.hmacShaKeyFor(
        "ThisIsASecretKeyForJWTGenerationChangeMe123456".getBytes()
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Let login/register through without a token
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendUnauthorized(response, "Invalid or expired token");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}