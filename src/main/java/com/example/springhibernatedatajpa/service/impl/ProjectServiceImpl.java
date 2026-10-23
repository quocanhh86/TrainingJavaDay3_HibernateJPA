package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.infrastructure.service.IFileService;
import com.example.springhibernatedatajpa.dao.IDepartmentDao;
import com.example.springhibernatedatajpa.dao.IEmployeeDao;
import com.example.springhibernatedatajpa.dao.IProjectDao;
import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.entity.Project;
import com.example.springhibernatedatajpa.model.request.ProjectRequest;
import com.example.springhibernatedatajpa.model.response.ProjectResponse;
import com.example.springhibernatedatajpa.service.IProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final IProjectDao projectDao;
    private final IEmployeeDao employeeDao;
    private final IFileService fileService;
    private final IDepartmentDao departmentDao;

    @Override
    public List<ProjectResponse> getProjects() {
        List<Project> projects = projectDao.findAll();
        List<ProjectResponse> projectResponseList = new ArrayList<>();
        for (Project project : projects) {
            ProjectResponse projectResponse = mapToResponse(project);
            projectResponseList.add(projectResponse);
        }

        return projectResponseList;
    }

    @Override
    public ProjectResponse getProjectById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id không được để trống");
        }

        Project project = projectDao.findById(id);
        if (project == null) {
            throw new NoSuchElementException("dự án không tồn tại");
        }

        return mapToResponse(project);
    }

    @Override
    public void addProject(ProjectRequest projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setBudget(projectDto.getBudget());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());

        Employee employee = employeeDao.getEmployee(projectDto.getTeamLeaderId());
        if (employee != null) {
            project.setTeamLead(employee);
        }

        if (projectDto.getContractFile() != null && !projectDto.getContractFile().isEmpty()) {
            String contractUrl = fileService.uploadFile(projectDto.getContractFile(), "contract");
            project.setContractUrl(contractUrl);
        }

        Department department = departmentDao.findById(projectDto.getDepartmentId());
        if (department != null) {
            project.setDepartment(department);
        } else {
            throw new NoSuchElementException("Vui lòng chọn đúng phòng ban");
        }

        projectDao.add(project);
    }

    @Override
    public void updateProject(ProjectRequest projectDto) {
        Project project = projectDao.findById(projectDto.getId());
        if (project == null) {
            throw new NoSuchElementException("Dự án không tồn tại hoặc sai id");
        }

        project.setName(projectDto.getName());
        project.setBudget(projectDto.getBudget());
        project.setStartDate(projectDto.getStartDate());
        project.setEndDate(projectDto.getEndDate());

        Employee employee = employeeDao.getEmployee(projectDto.getTeamLeaderId());
        if (employee != null) {
            project.setTeamLead(employee);
        }

        if (projectDto.getContractFile() != null && !projectDto.getContractFile().isEmpty()) {
            String contractUrl = fileService.uploadFile(projectDto.getContractFile(), "contract");
            project.setContractUrl(contractUrl);
        }

        Department department = departmentDao.findById(projectDto.getDepartmentId());
        if (department != null) {
            project.setDepartment(department);
        } else {
            throw new NoSuchElementException("Vui lòng chọn đúng phòng ban");
        }
        projectDao.update(project);
    }

    @Override
    public void deleteProject(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id không chính xác");
        }

        Project project = projectDao.findById(id);
        if (project == null) {
            throw new NoSuchElementException("Dự án không tồn tại hoặc sai id");
        }

        projectDao.delete(id);
    }

    private ProjectResponse mapToResponse(Project project) {
        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(project.getId());
        projectResponse.setName(project.getName());
        projectResponse.setBudget(project.getBudget());
        projectResponse.setStartDate(project.getStartDate());
        projectResponse.setEndDate(project.getEndDate());
        projectResponse.setStatus(project.getStatus());

        if (project.getDepartment() != null) {
            projectResponse.setDepartmentId(project.getDepartment().getId());
            projectResponse.setDepartmentName(project.getDepartment().getName());
        }

        projectResponse.setContractUrl(project.getContractUrl());
        projectResponse.setTeamLead(project.getTeamLead());
        projectResponse.setEmployees(project.getEmployees());

        return projectResponse;
    }
}
