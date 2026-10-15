package com.example.springhibernatedatajpa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageCustomController {

    @GetMapping("/401")
    public String error401() {
        return "fragments/401";
    }

    @GetMapping("/403")
    public String error403() {
        return "fragments/403";
    }

    @GetMapping("/500")
    public String error500() {
        return "fragments/500";
    }

    @GetMapping("/maintenance")
    public String maintenance() {
        return "fragments/maintaince";
    }
}
