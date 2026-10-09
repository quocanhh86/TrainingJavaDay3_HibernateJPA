package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.model.DepartmentDto;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements IDepartmentService {

    private final IDepartmentDao departmentDao;

    @Override
    public List<DepartmentDto> findAll() {
        List<Department> departments = departmentDao.findAll();
        List<DepartmentDto> departmentDtos = new ArrayList<>();

        for(Department department : departments)
        {
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setDepartmentName(department.getName());
            departmentDto.setId(department.getId());
            departmentDto.setDescription(department.getDescription());
            departmentDtos.add(departmentDto);
        }

        return departmentDtos;
    }

    @Override
    public DepartmentDto findById(UUID id) {
        return null;
    }

    @Override
    public void addDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        department.setName(departmentDto.getDepartmentName());
        if(departmentDto.getDescription() != null && !departmentDto.getDescription().isBlank())
        {
            department.setDescription(departmentDto.getDescription());

        }

        departmentDao.save(department);
    }

    @Override
    public void updateDepartment(DepartmentDto departmentDto) {
        Department department = departmentDao.findById(departmentDto.getId());
        department.setName(departmentDto.getDepartmentName());
        if(departmentDto.getDescription() != null && !departmentDto.getDescription().isBlank())
        {
            department.setDescription(departmentDto.getDescription());

        }

        departmentDao.update(department);
    }

    @Override
    public void removeDepartment(UUID id) {
        if(id != null)
        {
            departmentDao.delete(id);
        }
    }
}
