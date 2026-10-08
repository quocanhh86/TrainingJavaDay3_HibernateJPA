package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.EmployeeDto;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
    List<EmployeeDto> findAll();
    EmployeeDto findById(UUID id);
    void addEmployee(EmployeeDto employee);
    void updateEmployee(EmployeeDto employee);
    void deleteById(UUID id);
}
