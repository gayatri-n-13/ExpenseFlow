package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_hashesPasswordBeforeSaving() {
        when(passwordEncoder.encode("pass123")).thenReturn("HASHED");
        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        User saved = authService.register("riya", "pass123");

        assertEquals("riya", saved.getUsername());
        assertEquals("HASHED", saved.getPassword());
        assertNotEquals("pass123", saved.getPassword());
    }

    @Test
    void login_withCorrectPassword_returnsToken() {
        User user = new User("riya", "HASHED");
        when(userRepository.findByUsername("riya")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "HASHED")).thenReturn(true);
        when(jwtUtil.generateToken("riya")).thenReturn("FAKE.JWT.TOKEN");

        String token = authService.login("riya", "pass123");

        assertEquals("FAKE.JWT.TOKEN", token);
    }

    @Test
    void login_withWrongPassword_throwsAndIssuesNoToken() {
        User user = new User("riya", "HASHED");
        when(userRepository.findByUsername("riya")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "HASHED")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("riya", "wrong"));

        assertEquals("Invalid credentials", ex.getMessage());
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void login_withUnknownUser_throws() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login("ghost", "pass123"));

        assertEquals("User not found", ex.getMessage());
    }
}