package com.example.springhibernatedatajpa.model.response;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

public class ProjectResponse {
    private UUID id;
    private String name;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID departmentId;
    private String departmentName;
}
