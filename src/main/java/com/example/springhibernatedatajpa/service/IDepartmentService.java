package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.request.DepartmentRequest;
import com.example.springhibernatedatajpa.model.response.DepartmentResponse;

import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    List<DepartmentResponse> findAll();
    DepartmentResponse findById(UUID id);
    void addDepartment(DepartmentRequest departmentRequest);
    void updateDepartment(DepartmentRequest departmentRequest);
    void removeDepartment(UUID id);
}
