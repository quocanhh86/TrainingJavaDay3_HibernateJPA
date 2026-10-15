package com.example.springhibernatedatajpa.dao;

import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.UserAdmin;

public interface IAuthenticationDao {
    UserAdmin findByEmail(String email);
    void addUser(UserAdmin userAdmin);
    void addEmployee(Employee employee);
    Employee getEmployeeByEmail(String email);
}
