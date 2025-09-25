package com.EmployeeService.Employee.Service.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.services.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Operation(summary = "Create a new employee", description = "Saves an employee to the database")
    @ApiResponse(responseCode = "200", description = "Employee created successfully")
    @PostMapping
    public Employee create(@RequestBody Employee e) {
        return service.save(e);
    }

    @Operation(summary = "Get employee with department", description = "Retrieve an employee and their department by ID")
    @ApiResponse(responseCode = "200", description = "Employee and department found")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    @GetMapping("/{id}")
    public CombinedResponse get(@PathVariable Long id) {
        return service.getEmployeeWithDepartment(id);
    }
}