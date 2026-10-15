package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.model.request.DepartmentRequest;
import com.example.springhibernatedatajpa.model.response.DepartmentResponse;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements IDepartmentService {

    private final IDepartmentDao departmentDao;

    @Override
    public List<DepartmentResponse> findAll() {
        List<Department> departments = departmentDao.findAll();
        List<DepartmentResponse> departmentDtos = new ArrayList<>();
        for(Department department : departments)
        {
            DepartmentResponse departmentResponse = mapToResponse(department);
            departmentDtos.add(departmentResponse);
        }

        return departmentDtos;
    }

    @Override
    public DepartmentResponse findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Mã phòng ban (ID) không được để trống!");
        }
        Department department = departmentDao.findById(id);
        if (department == null) {
            throw new NoSuchElementException("Không tìm thấy phòng ban với ID: " + id);
        }

        return mapToResponse(department);
    }

    @Override
    public void addDepartment(DepartmentRequest departmentRequest) {
        if (departmentRequest == null || departmentRequest.getDepartmentName() == null || departmentRequest.getDepartmentName().isBlank()) {
            throw new IllegalArgumentException("Tên phòng ban không được để trống!");
        }
        Department department = new Department();
        department.setName(departmentRequest.getDepartmentName());
        if(departmentRequest.getDescription() != null && !departmentRequest.getDescription().isBlank())
        {
            department.setDescription(departmentRequest.getDescription());

        }

        departmentDao.save(department);
    }

    @Override
    public void updateDepartment(DepartmentRequest departmentDto) {
        if (departmentDto == null || departmentDto.getId() == null) {
            throw new IllegalArgumentException("Mã phòng ban không hợp lệ!");
        }
        Department department = departmentDao.findById(departmentDto.getId());
        if (department == null) {
            throw new NoSuchElementException("Không tìm thấy phòng ban cần cập nhật!");
        }
        department.setName(departmentDto.getDepartmentName());
        if(departmentDto.getDescription() != null && !departmentDto.getDescription().isBlank())
        {
            department.setDescription(departmentDto.getDescription());

        }

        departmentDao.update(department);
    }

    @Override
    public void removeDepartment(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Mã phòng ban (ID) không được để trống!");
        }
        Department department = departmentDao.findById(id);
        if (department == null) {
            throw new NoSuchElementException("Phòng ban cần xóa không tồn tại!");
        }

        departmentDao.delete(id);
    }

    private DepartmentResponse mapToResponse(Department department)
    {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setDepartmentName(department.getName());
        departmentResponse.setId(department.getId());
        departmentResponse.setDescription(department.getDescription());
        return departmentResponse;
    }
}
