package com.DepartmentService.Department.Service.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DepartmentService.Department.Service.model.Department;
import com.DepartmentService.Department.Service.repositories.DepartmentRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
	private DepartmentRepository repository;

	public DepartmentController(DepartmentRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public Department save(@RequestBody Department department) {
		return repository.save(department);
	}

//    @GetMapping("/{id}")
//    public ResponseEntity<Object> findById(@PathVariable Long id) {
//        return repository.findById(id)
//                .<ResponseEntity<Object>>map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(Map.of("error", "Department with id " + id + " not found")));
//    }

	@Operation(summary = "Get department by ID", description = "Retrieve a department by its ID")
	@ApiResponse(responseCode = "200", description = "Department found")
	@ApiResponse(responseCode = "404", description = "Department not found")
	@GetMapping("/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		return repository.findById(id).<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity
				.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Department with id " + id + " not found")));
	}

}