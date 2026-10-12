package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.request.EmployeeRequest;
import com.example.springhibernatedatajpa.model.response.EmployeeResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface IEmployeeService {
    List<EmployeeResponse> findAll();
    EmployeeResponse findById(UUID id);
    void addEmployee(EmployeeRequest employee) throws IOException;
    void updateEmployee(EmployeeRequest employee) throws IOException;
    void deleteById(UUID id);
}
