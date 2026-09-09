package com.clinic;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123"; // <-- CHANGE THIS to your desired password
        String hash = encoder.encode(password);
        System.out.println("Plain: " + password);
        System.out.println("BCrypt: " + hash);
    }
}