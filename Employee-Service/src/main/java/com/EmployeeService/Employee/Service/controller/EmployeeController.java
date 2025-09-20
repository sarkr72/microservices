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

//@RestController
//@RequestMapping("/employees")
//public class EmployeeController {
//    private final EmployeeRepository repository;
//    private final DepartmentClient departmentClient;
//
//    public EmployeeController(EmployeeRepository repository, DepartmentClient departmentClient) {
//        this.repository = repository;
//        this.departmentClient = departmentClient;
//    }
//
//    @PostMapping
//    public Employee save(@RequestBody Employee employee) {
//        return repository.save(employee);
//    }
//
//    @GetMapping("/{id}")
//    public Map<String, Object> findById(@PathVariable Long id) {
//        Employee employee = repository.findById(id).orElse(null);
//
//        if (employee == null) return Map.of("message", "Employee not found");
//
//        Department department = departmentClient.getDepartment(employee.getDepartmentId());
//
//        return Map.of(
//            "employee", employee,
//            "department", department
//        );
//    }
//    
//    @GetMapping
//    public List<Employee> getAll() {
//        return repository.findAll();
//    }
//}

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    @PostMapping
    public Employee create(@RequestBody Employee e) { return service.save(e); }

    @GetMapping("/{id}")
    public CombinedResponse get(@PathVariable Long id) { return service.getEmployeeWithDepartment(id); }
}

