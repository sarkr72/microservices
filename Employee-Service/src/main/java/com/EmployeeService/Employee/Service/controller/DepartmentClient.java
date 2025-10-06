package com.EmployeeService.Employee.Service.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.EmployeeService.Employee.Service.dto.Department;

//@FeignClient(name = "department-service")
//public interface DepartmentClient {
//    @GetMapping("/departments/{id}")
//    Department getDepartment(@PathVariable("id") Long id);
//}

//@FeignClient(name = "department-service", url = "http://department-service:8080")
//public interface DepartmentClient {
//    @GetMapping("/departments/{id}")
//    Department getDepartment(@PathVariable("id") Long id);
//}

@FeignClient(name = "department-service") // Do NOT set 'url'
public interface DepartmentClient {
    @GetMapping("/departments/{id}")
    Department getDepartment(@PathVariable("id") Long id);
}
