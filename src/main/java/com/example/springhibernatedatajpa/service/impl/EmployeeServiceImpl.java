package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.model.EmployeeDto;
import com.example.springhibernatedatajpa.model.request.EmployeeRequest;
import com.example.springhibernatedatajpa.model.response.EmployeeResponse;
import com.example.springhibernatedatajpa.service.IEmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeDao employeeDao;
    private final IDepartmentDao departmentDao;

    @Override
    public List<EmployeeResponse> findAll() {
        List<Employee> employeesList = employeeDao.getEmployees();
        List<EmployeeResponse> employeeResponsesList = new ArrayList<>();
        for (Employee employee : employeesList) {
            log.info(employee.getAvatar());
            employeeResponsesList.add(mapToResponse(employee));
        }

        return employeeResponsesList;
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        Employee employee = employeeDao.getEmployee(id);
        return mapToResponse(employee);
    }

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) {
        Employee employeeEntity = new Employee();

        employeeEntity.setFirstName(employeeRequest.getFirstName());
        employeeEntity.setLastName(employeeRequest.getLastName());
        employeeEntity.setEmail(employeeRequest.getEmail());
        employeeEntity.setAddress(employeeRequest.getAddress());
        employeeEntity.setAge(employeeRequest.getAge());
        employeeEntity.setPhoneNumber(employeeRequest.getPhoneNumber());

        String fileUrl = uploadFile(employeeRequest.getAvatar());
        if(fileUrl != null) {
            employeeEntity.setAvatar(fileUrl);
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentDao.findById(employeeRequest.getDepartmentId());
            employeeEntity.setDepartment(department);
        }
        employeeDao.addEmployee(employeeEntity);
    }

    @Override
    public void updateEmployee(EmployeeRequest employeeRequest) {
        Employee employeeEntity = employeeDao.getEmployee(employeeRequest.getId());

        employeeEntity.setFirstName(employeeRequest.getFirstName());
        employeeEntity.setLastName(employeeRequest.getLastName());
        employeeEntity.setEmail(employeeRequest.getEmail());
        employeeEntity.setAddress(employeeRequest.getAddress());
        employeeEntity.setAge(employeeRequest.getAge());
        employeeEntity.setPhoneNumber(employeeRequest.getPhoneNumber());

        String fileUrl = uploadFile(employeeRequest.getAvatar());
        if(fileUrl != null) {
            employeeEntity.setAvatar(fileUrl);
        }
        else
        {
            employeeEntity.setAvatar(employeeRequest.getCurrentAvatar());
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentDao.findById(employeeRequest.getDepartmentId());
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

    public String uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            String ext =  Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + ext;
            File uploadDir = new File("src/main/resources/static/img");
            File destFile = new File(uploadDir, fileName);
            try {
                file.transferTo(destFile);
                return fileName;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public EmployeeResponse mapToResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employee.getId());
        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setAddress(employee.getAddress());
        employeeResponse.setAge(employee.getAge());
        employeeResponse.setPhoneNumber(employee.getPhoneNumber());
        employeeResponse.setAvatarUrl(employee.getAvatar());

        if (employee.getDepartment() != null) {
            employeeResponse.setDepartmentName(employee.getDepartment().getName());
            employeeResponse.setDepartmentId(employee.getDepartment().getId());
        }

        return employeeResponse;
    }
}
