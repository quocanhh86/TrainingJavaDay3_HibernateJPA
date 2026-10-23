package com.example.springhibernatedatajpa.dao;

import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.UserAdmin;

public interface IAuthenticationDao {
    UserAdmin getAdminByEmail(String email);
    void employeeRegister(Employee employee);
    Employee getEmployeeByEmail(String email);
}
