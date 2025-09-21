package com.teleauro.authentication.controller;

import com.teleauro.authentication.dto.LoginRequest;
import com.teleauro.authentication.model.User;
import com.teleauro.authentication.repository.UserRepository;
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
public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {


    System.out.println("Login attempt for user: " + loginRequest.getUsername());

    Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

    if (userOpt.isPresent()) {
        User user = userOpt.get();
        System.out.println("User found. Stored hash: " + user.getPasswordHash());
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
        System.out.println("Password matches: " + matches);

        if (matches) {
            return ResponseEntity.ok("Login successful");
        }
    } else {
        System.out.println("User not found.");
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
}


    @GetMapping("/test")
    public ResponseEntity<String> test() {
    return ResponseEntity.ok("AuthController is active");
}


}
