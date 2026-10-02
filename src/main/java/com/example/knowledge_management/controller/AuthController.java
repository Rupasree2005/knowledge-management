package com.example.knowledge_management.controller;

import com.example.knowledge_management.dto.LoginRequest;
import com.example.knowledge_management.dto.LoginResponse;
import com.example.knowledge_management.dto.UserRegistrationRequest;
import com.example.knowledge_management.dto.UserRegistrationResponse;
import com.example.knowledge_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        UserRegistrationResponse response =
                userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = userService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        request.getUsername(),
                        "USER"
                )
        );
    }
}