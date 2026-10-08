package com.example.springhibernatedatajpa.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjectDto {
    private UUID id;
    private String name;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID departmentId;
}
