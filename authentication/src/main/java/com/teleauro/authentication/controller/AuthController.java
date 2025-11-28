
package com.teleauro.authentication.controller;

import com.teleauro.authentication.dto.LoginRequest;
import com.teleauro.authentication.dto.LoginResponse;
import com.teleauro.authentication.model.User;
import com.teleauro.authentication.repository.UserRepository;
import com.teleauro.authentication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User newUser) {
        if (userRepository.existsById(newUser.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(newUser.getPasswordHash());
        newUser.setPasswordHash(hashedPassword);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        long start = System.currentTimeMillis();

        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
            if (matches) {
                long jwtStart = System.currentTimeMillis();
                String token = jwtUtil.generateToken(user.getUsername());
                long jwtDuration = System.currentTimeMillis() - jwtStart;

                long totalDuration = System.currentTimeMillis() - start;

                // Get expiration from JwtUtil
                long expiresIn = jwtUtil.getExpiration(); // in milliseconds

                LoginResponse response = new LoginResponse("Login successful", token, jwtDuration, totalDuration, expiresIn);
                return ResponseEntity.ok(response);
            }
        }

        long totalDuration = System.currentTimeMillis() - start;
        LoginResponse response = new LoginResponse("Login failed", "", 0, totalDuration, 0);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("AuthController is active");
    }
}
