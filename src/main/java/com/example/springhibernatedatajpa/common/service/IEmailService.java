package com.example.springhibernatedatajpa.common.service;

public interface IEmailService {
    void sendEmail(String toEmail, String subject, String body);
}
