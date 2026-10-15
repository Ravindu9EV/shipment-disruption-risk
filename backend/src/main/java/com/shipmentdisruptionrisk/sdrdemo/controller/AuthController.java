package com.shipmentdisruptionrisk.sdrdemo.controller;

import com.shipmentdisruptionrisk.sdrdemo.Utility.Status;
import com.shipmentdisruptionrisk.sdrdemo.dto.LoginRequestDTO;
import com.shipmentdisruptionrisk.sdrdemo.dto.LoginResponseDTO;
import com.shipmentdisruptionrisk.sdrdemo.entity.User;
import com.shipmentdisruptionrisk.sdrdemo.repository.UserRepository;
import com.shipmentdisruptionrisk.sdrdemo.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequestDTO request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        if (user.getStatus() != Status.ACTIVE) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        LoginResponseDTO response = new LoginResponseDTO(token, user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(response);
    }
}
