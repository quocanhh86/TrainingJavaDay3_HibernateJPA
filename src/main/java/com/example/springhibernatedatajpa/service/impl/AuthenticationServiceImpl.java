package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.infrastructure.exception.DuplicateResourceException;
import com.example.springhibernatedatajpa.dao.IAuthenticationDao;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.UserAdmin;
import com.example.springhibernatedatajpa.model.request.RegisterRequest;
import com.example.springhibernatedatajpa.service.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final IAuthenticationDao authenticationDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerEmployee(RegisterRequest registerRequest) {
        UserAdmin userAdminExist = authenticationDao.getAdminByEmail(registerRequest.getEmail());
        if (userAdminExist != null) {
            throw new DuplicateResourceException("Tài khoản đã tồn tại trong hệ thống, vui lòng đăng nhập!");
        }

        Employee employeeExist = authenticationDao.getEmployeeByEmail(registerRequest.getEmail());
        if (employeeExist != null) {
            throw new DuplicateResourceException("Tài khoản đã tồn tại trong hệ thống, vui lòng đăng nhập!");
        }

        Employee employee = Employee.builder()
                .firstName(registerRequest.getFirstName() != null ? registerRequest.getFirstName().trim() : "")
                .lastName(registerRequest.getLastName() != null ? registerRequest.getLastName().trim() : "")
                .email(registerRequest.getEmail().trim())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        authenticationDao.employeeRegister(employee);
    }
}
