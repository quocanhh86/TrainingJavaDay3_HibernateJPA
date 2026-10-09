package com.example.springhibernatedatajpa.service.impl;

import com.example.springhibernatedatajpa.dao.IProjectDao;
import com.example.springhibernatedatajpa.entity.Project;
import com.example.springhibernatedatajpa.model.ProjectDto;
import com.example.springhibernatedatajpa.service.IProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final IProjectDao projectDao;


    @Override
    public List<ProjectDto> getProjects() {

        return List.of();
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        return null;
    }

    @Override
    public void addProject(ProjectDto projectDto) {

    }

    @Override
    public void updateProject(ProjectDto projectDto) {

    }

    @Override
    public void deleteProject(Long id) {

    }

    private ProjectDto mapToDto(Project Project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(Project.getId());
        projectDto.setName(Project.getName());
        projectDto.setBudget(Project.getBudget());
        projectDto.setStartDate(Project.getStartDate());
        projectDto.setEndDate(Project.getEndDate());
        if (Project.getDepartment() != null) {
            projectDto.setDepartmentId(Project.getDepartment().getId());
        }

        return projectDto;
    }
}
