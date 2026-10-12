package com.example.springhibernatedatajpa.service;

import com.example.springhibernatedatajpa.model.request.ProjectRequest;
import com.example.springhibernatedatajpa.model.response.ProjectResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.UUID;

public interface IProjectService {
    List<ProjectResponse> getProjects();
    ProjectResponse getProjectById(UUID id);
    void addProject(ProjectRequest projectDto);
    void updateProject(ProjectRequest projectDto);
    void deleteProject(UUID id);
}
