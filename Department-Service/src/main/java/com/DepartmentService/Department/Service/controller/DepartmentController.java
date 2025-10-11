package com.departmentService.department.service.controller;

import com.departmentService.department.service.model.Department;
import com.departmentService.department.service.repository.DepartmentRepository;
import com.departmentService.department.service.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    private final DepartmentRepository repository;
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentRepository repository, DepartmentService departmentService) {
        this.repository = repository;
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<Department> save(@RequestBody Department department) {
        Department savedDepartment = repository.save(department);
        try {
            departmentService.createDepartment(savedDepartment.getId(), savedDepartment.getName());
        } catch (MessageDeliveryException e) {
            logger.error("Failed to publish DepartmentCreatedEvent for departmentId={}: {}", 
                        savedDepartment.getId(), e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return repository.findById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Department with id " + id + " not found")));
    }

    @GetMapping
    public ResponseEntity<List<Department>> findAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}