package com.example.springhibernatedatajpa.dao;

import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.model.EmployeeDto;

import java.util.List;
import java.util.UUID;

public interface IEmployeeDao {
    List<Employee> getEmployees();
    Employee getEmployee(UUID id);
    void addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(UUID id);
}
