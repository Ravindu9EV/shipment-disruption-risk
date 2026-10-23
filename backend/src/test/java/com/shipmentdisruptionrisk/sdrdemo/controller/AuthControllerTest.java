package com.shipmentdisruptionrisk.sdrdemo.controller;


import com.shipmentdisruptionrisk.sdrdemo.Utility.Roles;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import com.shipmentdisruptionrisk.sdrdemo.repository.UserRepository;
import com.shipmentdisruptionrisk.sdrdemo.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @MockitoBean private UserRepository userRepository;
    @MockitoBean private PasswordEncoder passwordEncoder;
    @MockitoBean private JwtService jwtService;

    @Autowired private MockMvc mockMvc;


    @Test
    void login_shouldReturnTokenWhenCredentialsAreValid() throws Exception {
        // Arrange
        User user = new User("abc", "hashedPassword", "abc@gmail.com", Roles.USER);

        when(userRepository.findByUsername("abc")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken("abc", "USER")).thenReturn("mockToken");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "abc",
                            "password": "plainPassword"
                        }
                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mockToken"))
            .andExpect(jsonPath("$.username").value("abc"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void login_shouldReturn401WhenPasswordIsWrong() throws Exception {
        // Arrange
        User user = new User("abc", "hashedPassword", "abc@gmail.com", Roles.USER);

        when(userRepository.findByUsername("abc")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "abc",
                            "password": "wrongPassword"
                        }
                        """))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

    @Test
    void login_shouldReturn401WhenUserDoesNotExist() throws Exception {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "username": "unknown",
                            "password": "anyPassword"
                        }
                        """))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.error").value("Invalid username or password"));
    }

}
