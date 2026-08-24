package com.clinic.service;

import com.clinic.dto.LoginRequestDto;
import com.clinic.dto.RegisterRequestDto;
import com.clinic.dto.UserResponseDto;
import com.clinic.entity.User;
import com.clinic.exception.DuplicateEmailException;
import com.clinic.exception.UserNotFoundException;
import com.clinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(RegisterRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new DuplicateEmailException("Username " + requestDto.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateEmailException("Email " + requestDto.getEmail() + " already exists");
        }

        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        User user = User.builder()
                .userId(userId)
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(requestDto.getRole())
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        return mapToResponseDto(saved);
    }

    @Override
    public UserResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + requestDto.getUsername()));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid password");
        }

        return mapToResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return mapToResponseDto(user);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}