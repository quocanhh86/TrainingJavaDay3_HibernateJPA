package com.example.springhibernatedatajpa.infrastructure.service;

public interface IEmailService {
    void sendEmail(String toEmail, String subject, String body);
}
