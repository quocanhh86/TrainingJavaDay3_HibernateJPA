package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.entity.Department;
import com.example.springhibernatedatajpa.model.DepartmentDto;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {
    private final IDepartmentService departmentService;

    @GetMapping()
    public String departmentPage(Model model) {
        List<DepartmentDto> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "department/home-department";
    }

    @GetMapping("/{id}")
    public String departmentDetail(@PathVariable UUID id, Model model) {
        DepartmentDto departmentDto = departmentService.findById(id);
        model.addAttribute("department", departmentDto);
        return "department/home-department";
    }

    @PostMapping()
    public String addDepartment(@ModelAttribute DepartmentDto departmentDto) {
        departmentService.addDepartment(departmentDto);
        return "redirect:/department";
    }

    @PostMapping("/update")
    public String updateDepartment(@ModelAttribute DepartmentDto departmentDto) {
        departmentService.updateDepartment(departmentDto);
        return "redirect:/department";
    }

    @PostMapping("/remove")
    public String removeDepartment(@ModelAttribute DepartmentDto departmentDto) {
        departmentService.removeDepartment(departmentDto.getId());
        return "redirect:/department";
    }
}
