package com.EmployeeService.Employee.Service.controller;

import com.EmployeeService.Employee.Service.dto.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "department-service")
public interface DepartmentClient {
    @GetMapping("/departments/{id}")
    Department getDepartment(@PathVariable("id") Long id);
}