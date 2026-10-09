package com.example.springhibernatedatajpa.controller;

import com.example.springhibernatedatajpa.entity.Employee;
import com.example.springhibernatedatajpa.model.DepartmentDto;
import com.example.springhibernatedatajpa.model.EmployeeDto;
import com.example.springhibernatedatajpa.service.IDepartmentService;
import com.example.springhibernatedatajpa.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        List<EmployeeDto> employeeList = employeeService.findAll();
        model.addAttribute("employeeList", employeeList);
        return "employee/home-employee";
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(@ModelAttribute("employeeDto") EmployeeDto employeeDto, Model model) {
        List<DepartmentDto> departmentDtoList = departmentService.findAll();
        model.addAttribute("departmentList", departmentDtoList);
        return "employee/add-employee";
    }

    @GetMapping("/{id}")
    public String getEmployeeById(@PathVariable UUID id, Model model) {
        EmployeeDto  employeeDto = employeeService.findById(id);
        model.addAttribute("employee", employeeDto);
        return "employee/home-employee";
    }

    @PostMapping()
    public String createEmployee(@Validated @ModelAttribute("employeeDto") EmployeeDto employeeDto, 
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "employee/add-employee";
        }
        employeeService.addEmployee(employeeDto);
        return "redirect:/employee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute EmployeeDto employeeDto) {
        employeeService.updateEmployee(employeeDto);
        return "redirect:/employee";
    }

    @PostMapping("/remove/{id}")
    public String removeEmployee(@PathVariable UUID id)
    {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }
}
