package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.infrastructure.constant.PathAuthenticationConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageCustomController {

    @GetMapping(PathAuthenticationConstant.ERROR_401)
    public String error401() {
        return "fragments/401";
    }

    @GetMapping(PathAuthenticationConstant.ERROR_403)
    public String error403() {
        return "fragments/403";
    }

    @GetMapping(PathAuthenticationConstant.ERROR_500)
    public String error500() {
        return "fragments/500";
    }

    @GetMapping(PathAuthenticationConstant.MAINTAIN_PREFIX)
    public String maintenance() {
        return "fragments/maintaince";
    }
}
