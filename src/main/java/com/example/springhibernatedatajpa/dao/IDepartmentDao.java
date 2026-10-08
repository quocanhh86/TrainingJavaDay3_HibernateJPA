package com.example.springhibernatedatajpa.dao;

import com.example.springhibernatedatajpa.entity.Department;

import java.util.List;
import java.util.UUID;

public interface IDepartmentDao {
    List<Department> findAll();
    Department findById(UUID id);
    void save(Department department);
    void update(Department department);
    void delete(UUID id);
}
