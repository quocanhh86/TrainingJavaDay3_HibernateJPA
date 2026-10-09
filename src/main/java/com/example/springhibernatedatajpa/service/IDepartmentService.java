package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.model.DepartmentDto;

import java.util.List;
import java.util.UUID;

public interface IDepartmentService {
    List<DepartmentDto> findAll();
    DepartmentDto findById(UUID id);
    void addDepartment(DepartmentDto departmentDto);
    void updateDepartment(DepartmentDto departmentDto);
    void removeDepartment(UUID id);
}
