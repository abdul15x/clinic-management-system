package com.clinic.service;

import com.clinic.dto.LoginRequestDto;
import com.clinic.dto.RegisterRequestDto;
import com.clinic.dto.UserResponseDto;
import com.clinic.entity.User;
import com.clinic.enums.Role;
import com.clinic.exception.UserNotFoundException;
import com.clinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegisterRequestDto registerDto;
    private LoginRequestDto loginDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("mongo-id-789");
        user.setUserId("USR-I9J0K1L2");
        user.setUsername("admin");
        user.setEmail("admin@clinic.com");
        user.setPassword("encoded-password");
        user.setRole(Role.ADMIN);
        user.setCreatedAt(LocalDateTime.now());

        registerDto = new RegisterRequestDto();
        registerDto.setUsername("newuser");
        registerDto.setEmail("newuser@clinic.com");
        registerDto.setPassword("password123");
        registerDto.setRole(Role.RECEPTIONIST);

        loginDto = new LoginRequestDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("admin123");

        lenient().when(userRepository.findById("admin")).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    }

    @Test
    void register_Success() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto result = userService.register(registerDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("admin");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> userService.register(registerDto));
    }

    @Test
    void login_Success() {
        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

        UserResponseDto result = userService.login(loginDto);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("admin");
        assertThat(result.getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.login(loginDto));

        assertThat(exception.getMessage()).contains("Invalid");
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userService.login(loginDto));

        assertThat(exception.getMessage()).contains("not found");
    }

    @Test
    void getUserByUsername_Success() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        UserResponseDto result = userService.getUserByUsername("admin");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("admin");
    }

    @Test
    void getUserByUsername_NotFound_ThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserByUsername("unknown"));
    }
}