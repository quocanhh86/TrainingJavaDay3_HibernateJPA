package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.EmployeeDto;
import com.example.springhibernatedatajpa.model.request.EmployeeRequest;
import com.example.springhibernatedatajpa.model.response.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
    List<EmployeeResponse> findAll();
    EmployeeResponse findById(UUID id);
    void addEmployee(EmployeeRequest employee);
    void updateEmployee(EmployeeRequest employee);
    void deleteById(UUID id);
}
