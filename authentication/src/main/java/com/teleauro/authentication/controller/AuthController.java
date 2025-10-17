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

        long dbStart = System.currentTimeMillis();
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        long dbDuration = System.currentTimeMillis() - dbStart;
        System.out.println("DB query took " + dbDuration + "ms");

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            long hashStart = System.currentTimeMillis();
            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
            long hashDuration = System.currentTimeMillis() - hashStart;
            System.out.println("Password match took " + hashDuration + "ms");

            if (matches) {
                long jwtStart = System.currentTimeMillis();
                String token = jwtUtil.generateToken(user.getUsername());
                long jwtDuration = System.currentTimeMillis() - jwtStart;
                System.out.println("JWT generation took " + jwtDuration + "ms");

                long totalDuration = System.currentTimeMillis() - start;
                System.out.println("Login successful in " + totalDuration + "ms");

                LoginResponse response = new LoginResponse("Login successful", token, jwtDuration, totalDuration);
                return ResponseEntity.ok(response);
            }
        }

        long totalDuration = System.currentTimeMillis() - start;
        System.out.println("Login failed in " + totalDuration + "ms");

        LoginResponse response = new LoginResponse("Login failed", "", 0, totalDuration);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("AuthController is active");
    }
}
