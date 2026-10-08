package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.model.EmployeeDto;
import com.example.springhibernatedatajpa.service.IEmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeDao employeeDao;
    private final IDepartmentDao departmentDao;

    @Override
    public List<EmployeeDto> findAll() {
        List<Employee>  employees = employeeDao.getEmployees();
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeDto employeeDto = new EmployeeDto();
            employeeDto.setId(employee.getId());
            employeeDto.setFirstName(employee.getFirstName());
            employeeDto.setLastName(employee.getLastName());
            employeeDto.setEmail(employee.getEmail());
            employeeDto.setAddress(employee.getAddress());
            employeeDto.setAge(employee.getAge());
            employeeDto.setPhoneNumber(employee.getPhoneNumber());
            employeeDto.setAvatarUrl(employee.getAvatar());

            if (employee.getDepartment() != null) {
                employeeDto.setDepartmentId(employee.getDepartment().getId());
                employeeDto.setDepartmentName(employee.getDepartment().getName());
            }

            employeeDtos.add(employeeDto);
        }

        return employeeDtos;
    }

    @Override
    public EmployeeDto findById(UUID id) {
        Employee employee = employeeDao.getEmployee(id);

        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setAddress(employee.getAddress());
        employeeDto.setAge(employee.getAge());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());
        employeeDto.setAvatarUrl(employee.getAvatar());

        if (employee.getDepartment() != null) {
            employeeDto.setDepartmentId(employee.getDepartment().getId());
            employeeDto.setDepartmentName(employee.getDepartment().getName());
        }

        return employeeDto;
    }

    @Override
    public void addEmployee(EmployeeDto employee) {
        Employee employeeEntity = new Employee();

        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());
        employeeEntity.setEmail(employee.getEmail());
        employeeEntity.setAddress(employee.getAddress());
        employeeEntity.setAge(employee.getAge());
        employeeEntity.setPhoneNumber(employee.getPhoneNumber());
        employeeEntity.setAvatar(employee.getAvatarUrl());

        if (employee.getDepartmentId() != null) {
            Department department = departmentDao.findById(employee.getDepartmentId());
            employeeEntity.setDepartment(department);
        }
        employeeDao.addEmployee(employeeEntity);
    }

    @Override
    public void updateEmployee(EmployeeDto employee) {
        Employee employeeEntity = employeeDao.getEmployee(employee.getId());

        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName(employee.getLastName());
        employeeEntity.setEmail(employee.getEmail());
        employeeEntity.setAddress(employee.getAddress());
        employeeEntity.setAge(employee.getAge());
        employeeEntity.setPhoneNumber(employee.getPhoneNumber());
        employeeEntity.setAvatar(employee.getAvatarUrl());

        if (employee.getDepartmentId() != null) {
            Department department = departmentDao.findById(employee.getDepartmentId());
            employeeEntity.setDepartment(department);
        } else {
            employeeEntity.setDepartment(null);
        }
        employeeDao.updateEmployee(employeeEntity);
    }

    @Override
    public void deleteById(UUID id) {
        employeeDao.deleteEmployee(id);
    }
}
