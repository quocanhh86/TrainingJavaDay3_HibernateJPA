package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.model.request.DepartmentRequest;
import com.example.springhibernatedatajpa.model.response.DepartmentResponse;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
        List<DepartmentResponse> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        return "department/home";
    }

    @GetMapping("/{id}")
    public String departmentDetail(@PathVariable UUID id, Model model) {
        List<DepartmentResponse> departments = departmentService.findAll();
        model.addAttribute("departments", departments);
        DepartmentResponse departmentResponse = departmentService.findById(id);
        model.addAttribute("department", departmentResponse);
        return "department/home";
    }

    @GetMapping("/add")
    public String showAddDepartment(@ModelAttribute("departmentRequest") DepartmentRequest departmentRequest) {
        return "department/add";
    }

    @PostMapping()
    public String addDepartment(@Validated @ModelAttribute("departmentRequest") DepartmentRequest departmentRequest,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "department/add";
        }

        departmentService.addDepartment(departmentRequest);
        return "redirect:/department";
    }

    @GetMapping("/edit/{id}")
    public String showEditDepartment(@PathVariable UUID id,
                                     @ModelAttribute("departmentRequest") DepartmentRequest departmentRequest,
                                     Model model) {
        DepartmentResponse departmentResponse = departmentService.findById(id);
        departmentRequest.setId(departmentResponse.getId());
        departmentRequest.setDepartmentName(departmentResponse.getDepartmentName());
        departmentRequest.setDescription(departmentResponse.getDescription());
        model.addAttribute("departmentRequest", departmentRequest);
        return "department/edit";
    }

    @PostMapping("/update")
    public String updateDepartment(@Validated @ModelAttribute("departmentRequest") DepartmentRequest departmentRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "department/edit";
        }
        departmentService.updateDepartment(departmentRequest);
        return "redirect:/department";
    }

    @PostMapping("/remove/{id}")
    public String removeDepartment(@PathVariable UUID id) {
        departmentService.removeDepartment(id);
        return "redirect:/department";
    }
}
