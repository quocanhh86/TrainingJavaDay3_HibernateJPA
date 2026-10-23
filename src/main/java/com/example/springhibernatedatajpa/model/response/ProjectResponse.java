package com.example.springhibernatedatajpa.model.response;

import com.example.springhibernatedatajpa.infrastructure.enums.Status;
import com.example.springhibernatedatajpa.entity.Employee;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProjectResponse {
    private UUID id;
    private String name;
    private Double budget;
    private LocalDate startDate;
    private LocalDate endDate;
    private UUID departmentId;
    private String departmentName;
    private String contractUrl;
    private Employee teamLead;
    private Status status;
    private Set<Employee> employees;
}
