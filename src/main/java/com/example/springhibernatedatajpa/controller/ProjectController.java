package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.infrastructure.constant.PathManagerConstant;
import com.example.springhibernatedatajpa.infrastructure.service.IFileService;
import com.example.springhibernatedatajpa.model.response.ProjectResponse;
import com.example.springhibernatedatajpa.service.IProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathManagerConstant.MANAGER_PREFIX)
public class ProjectController {

    private final IProjectService projectService;
    private final IFileService fileService;

    @GetMapping
    public String listProjects(Model model) {
        List<ProjectResponse> projects = projectService.getProjects();
        model.addAttribute("projects", projects);
        return "project/home";
    }

    @GetMapping("/{id}")
    public String getProject(@PathVariable UUID id, Model model) {
        List<ProjectResponse> projects = projectService.getProjects();
        model.addAttribute("projects", projects);
        ProjectResponse project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        return "project/home";
    }

    @GetMapping("/contract/{fileName}")
    public ResponseEntity<Resource> downloadContract(@PathVariable String fileName) {
        Resource resource = fileService.exportFile(fileName, "contract");
        String contentType = fileService.getContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
