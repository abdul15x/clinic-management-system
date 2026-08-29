package com.clinic.controller;

import com.clinic.dto.AuthResponseDto;
import com.clinic.dto.LoginRequestDto;
import com.clinic.dto.RegisterRequestDto;
import com.clinic.dto.UserResponseDto;
import com.clinic.security.JwtUtil;
import com.clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthRestController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "Register a new user", description = "Creates a new user account with role")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        return new ResponseEntity<>(userService.register(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Login user", description = "Authenticates user and returns JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        UserResponseDto user = userService.login(requestDto);
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        AuthResponseDto response = AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get current user", description = "Retrieves user details by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}