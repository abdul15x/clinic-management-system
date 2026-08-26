package com.clinic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/debug/auth")
    public Map<String, Object> debugAuth() {
        Map<String, Object> result = new HashMap<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            result.put("authenticated", false);
            result.put("message", "No authentication found in SecurityContext");
        } else {
            result.put("authenticated", auth.isAuthenticated());
            result.put("principal", auth.getName());
            result.put("authorities", auth.getAuthorities().toString());
            result.put("class", auth.getClass().getSimpleName());
        }

        return result;
    }
}