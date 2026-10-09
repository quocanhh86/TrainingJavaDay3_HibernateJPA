package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.ProjectDto;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public interface IProjectService {
    List<ProjectDto> getProjects();
    ProjectDto getProjectById(@ModelAttribute Long id);
    void addProject(@ModelAttribute ProjectDto projectDto);
    void updateProject(@ModelAttribute ProjectDto projectDto);
    void deleteProject(@ModelAttribute Long id);
}
