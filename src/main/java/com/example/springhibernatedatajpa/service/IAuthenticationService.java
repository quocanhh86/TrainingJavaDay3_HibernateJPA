package com.example.springhibernatedatajpa.service;
import com.example.springhibernatedatajpa.model.request.RegisterRequest;

public interface IAuthenticationService {
    void addEmployee(RegisterRequest registerRequest);
}
