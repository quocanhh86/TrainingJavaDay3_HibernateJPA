package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.infrastructure.constant.PathAuthenticationConstant;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @GetMapping(PathAuthenticationConstant.LOGIN_PREFIX)
    public String showLogin() {
        return "login";
    }

    @GetMapping(PathAuthenticationConstant.REGISTER_PREFIX)
    public String showRegister(@ModelAttribute RegisterRequest registerRequest) {
        return "register";
    }

    @PostMapping(PathAuthenticationConstant.REGISTER_PREFIX)
    public String register(@Validated @ModelAttribute RegisterRequest registerRequest,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        authenticationService.registerEmployee(registerRequest);
        return "redirect:/login";
    }

    @PostMapping(PathAuthenticationConstant.LOGIN_PREFIX)
    public String login(@Validated @ModelAttribute LoginRequest loginRequest,
                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        return "redirect:/";
    }
}
