package com.clinic.service;

import com.clinic.dto.LoginRequestDto;
import com.clinic.dto.RegisterRequestDto;
import com.clinic.dto.UserResponseDto;

public interface UserService {

    UserResponseDto register(RegisterRequestDto requestDto);

    UserResponseDto login(LoginRequestDto requestDto);

    UserResponseDto getUserByUsername(String username);
}