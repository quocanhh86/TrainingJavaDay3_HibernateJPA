package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.common.GlobalConfig;
import com.example.springhibernatedatajpa.common.service.IFileService;
import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.model.request.EmployeeRequest;
import com.example.springhibernatedatajpa.model.response.EmployeeResponse;
import com.example.springhibernatedatajpa.service.IEmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeDao employeeDao;
    private final IDepartmentDao departmentDao;
    private final IFileService fileService;

    @Override
    public List<EmployeeResponse> findAll() {
        List<Employee> employeesList = employeeDao.getEmployees();
        List<EmployeeResponse> employeeResponsesList = new ArrayList<>();
        for (Employee employee : employeesList) {
            employeeResponsesList.add(mapToResponse(employee));
        }

        return employeeResponsesList;
    }

    @Override
    public EmployeeResponse findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Mã nhân viên (ID) không được để trống!");
        }

        Employee employee = employeeDao.getEmployee(id);
        if (employee == null) {
            throw new NoSuchElementException("Không tìm thấy nhân viên với ID: " + id);
        }
        return mapToResponse(employee);
    }

    @Override
    public void addEmployee(EmployeeRequest employeeRequest) throws IOException {
        Employee employeeEntity = new Employee();
        employeeEntity.setFirstName(employeeRequest.getFirstName());
        employeeEntity.setLastName(employeeRequest.getLastName());
        employeeEntity.setEmail(employeeRequest.getEmail());
        employeeEntity.setAddress(employeeRequest.getAddress());
        employeeEntity.setAge(employeeRequest.getAge());
        employeeEntity.setPhoneNumber(employeeRequest.getPhoneNumber());

        String fileUrl = fileService.uploadFile(employeeRequest.getAvatar(), "img");
        if (fileUrl != null) {
            employeeEntity.setAvatar(fileUrl);
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentDao.findById(employeeRequest.getDepartmentId());
            if (department == null) {
                throw new NoSuchElementException("Phòng ban đã chọn không tồn tại!");
            }
            employeeEntity.setDepartment(department);
        }

        employeeDao.addEmployee(employeeEntity);
    }

    @Override
    public void updateEmployee(EmployeeRequest employeeRequest) throws IOException {
        if (employeeRequest.getId() == null) {
            throw new IllegalArgumentException("ID nhân viên cần cập nhật không hợp lệ!");
        }

        Employee employeeEntity = employeeDao.getEmployee(employeeRequest.getId());
        if (employeeEntity == null) {
            throw new NoSuchElementException("Không tìm thấy nhân viên cần cập nhật!");
        }

        employeeEntity.setFirstName(employeeRequest.getFirstName());
        employeeEntity.setLastName(employeeRequest.getLastName());
        employeeEntity.setEmail(employeeRequest.getEmail());
        employeeEntity.setAddress(employeeRequest.getAddress());
        employeeEntity.setAge(employeeRequest.getAge());
        employeeEntity.setPhoneNumber(employeeRequest.getPhoneNumber());

        String fileUrl = fileService.uploadFile(employeeRequest.getAvatar(), "img");
        if (fileUrl != null) {
            employeeEntity.setAvatar(fileUrl);
        } else {
            employeeEntity.setAvatar(employeeRequest.getCurrentAvatar());
        }

        if (employeeRequest.getDepartmentId() != null) {
            Department department = departmentDao.findById(employeeRequest.getDepartmentId());
            if (department == null) {
                throw new NoSuchElementException("Phòng ban đã chọn không tồn tại!");
            }
            employeeEntity.setDepartment(department);
        } else {
            employeeEntity.setDepartment(null);
        }
        employeeDao.updateEmployee(employeeEntity);
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Mã nhân viên (ID) không được để trống!");
        }

        Employee employee = employeeDao.getEmployee(id);
        if (employee == null) {
            throw new NoSuchElementException("Nhân viên cần xóa không tồn tại!");
        }

        employeeDao.deleteEmployee(id);
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
