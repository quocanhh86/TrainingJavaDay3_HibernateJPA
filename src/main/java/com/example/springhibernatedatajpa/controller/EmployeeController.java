package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.model.request.EmployeeRequest;
import com.example.springhibernatedatajpa.model.response.DepartmentResponse;
import com.example.springhibernatedatajpa.model.response.EmployeeResponse;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import com.example.springhibernatedatajpa.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    private final IEmployeeService  employeeService;
    private final IDepartmentService departmentService;

    @GetMapping()
    public String getAllEmployees(Model model) {
        List<EmployeeResponse> employeeList = employeeService.findAll();
        model.addAttribute("employeeList", employeeList);
        return "employee/home";
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(@ModelAttribute("employeeRequest") EmployeeRequest employeeRequest, Model model) {
        List<DepartmentResponse> departmentDtoList = departmentService.findAll();
        model.addAttribute("departmentList", departmentDtoList);
        return "employee/add";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable UUID id, Model model) {
        List<EmployeeResponse> employeeList = employeeService.findAll();
        model.addAttribute("employeeList", employeeList);
        EmployeeResponse employeeResponse = employeeService.findById(id);
        model.addAttribute("employee", employeeResponse);
        return "employee/home";
    }

    @PostMapping()
    public String createEmployee(@Validated @ModelAttribute("employeeRequest") EmployeeRequest employeeRequest,
                                 BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            List<DepartmentResponse> departmentList = departmentService.findAll();
            model.addAttribute("departmentList", departmentList);
            return "employee/add";
        }

        employeeService.addEmployee(employeeRequest);
        return "redirect:/employee";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateEmployee(@PathVariable UUID id, @ModelAttribute EmployeeRequest employeeRequest, Model model)
    {
        List<DepartmentResponse> departmentList = departmentService.findAll();

        EmployeeResponse employeeResponse = employeeService.findById(id);
        employeeRequest.setId(employeeResponse.getId());
        employeeRequest.setFirstName(employeeResponse.getFirstName());
        employeeRequest.setLastName(employeeResponse.getLastName());
        employeeRequest.setAddress(employeeResponse.getAddress());
        employeeRequest.setAge(employeeResponse.getAge());
        employeeRequest.setCurrentAvatar(employeeResponse.getAvatarUrl());
        employeeRequest.setEmail(employeeResponse.getEmail());
        employeeRequest.setPhoneNumber(employeeResponse.getPhoneNumber());
        employeeRequest.setDepartmentId(employeeResponse.getDepartmentId());

        model.addAttribute("employeeRequest", employeeRequest);
        model.addAttribute("departmentList", departmentList);
        return "employee/edit";

    }

    @PostMapping("/edit")
    public String updateEmployee(@Validated @ModelAttribute EmployeeRequest employeeRequest, BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasErrors())
        {
            List<DepartmentResponse> departmentList = departmentService.findAll();
            model.addAttribute("departmentList", departmentList);
            return "employee/edit";
        }

        employeeService.updateEmployee(employeeRequest);
        return "redirect:/employee";
    }

    @PostMapping("/remove/{id}")
    public String removeEmployee(@PathVariable UUID id)
    {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }
}
