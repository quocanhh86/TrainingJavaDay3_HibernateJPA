package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IAuthenticationDao;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.UserAdmin;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IAuthenticationDao authenticationDao;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("Tên đăng nhập không được để trống");
        }

        UserAdmin userAdmin = authenticationDao.findByEmail(username.trim());
        if (userAdmin != null) {
            return User.withUsername(userAdmin.getEmail())
                    .password(userAdmin.getPassword())
                    .roles("ADMIN")
                    .build();
        }

        Employee employee = authenticationDao.getEmployeeByEmail(username.trim());
        if (employee != null) {
            return User
                    .withUsername(employee.getEmail())
                    .password(employee.getPassword())
                    .roles("USER")
                    .build();
        }

        throw new UsernameNotFoundException("Không tìm thấy: " + username);
    }
}
