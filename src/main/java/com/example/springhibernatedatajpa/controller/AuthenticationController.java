package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.model.request.LoginRequest;
import com.example.springhibernatedatajpa.model.request.RegisterRequest;
import com.example.springhibernatedatajpa.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(@ModelAttribute RegisterRequest registerRequest) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        authenticationService.addEmployee(registerRequest);
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginRequest loginRequest,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        return "redirect:/";
    }
}
