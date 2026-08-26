package com.clinic.controller;

import com.clinic.dto.LoginRequestDto;
import com.clinic.dto.RegisterRequestDto;
import com.clinic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthViewController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDto());
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequestDto());
        return "auth/register";
    }

//    @PostMapping("/auth/login")
//    public String loginUser(@Valid @ModelAttribute("loginRequest") LoginRequestDto requestDto,
//                            BindingResult bindingResult,
//                            Model model) {
//        if (bindingResult.hasErrors()) {
//            return "auth/login";
//        }
//
//        try {
//            userService.login(requestDto);
//            return "redirect:/";
//        } catch (Exception e) {
//            model.addAttribute("globalError", e.getMessage());
//            return "auth/login";
//        }
//    }

    @PostMapping("/auth/register")
    public String registerUser(@Valid @ModelAttribute("registerRequest") RegisterRequestDto requestDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.register(requestDto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("globalError", e.getMessage());
            return "auth/register";
        }
    }
}