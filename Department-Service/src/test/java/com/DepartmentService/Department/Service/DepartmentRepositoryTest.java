package com.departmentService.department.service;

import com.departmentService.department.service.model.Department;
import com.departmentService.department.service.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository repository;

    @Test
    void testRepositoryBeanExists() {
        assertNotNull(repository, "DepartmentRepository bean should exist");
    }

    @Test
    void testSaveDepartment() {
        Department department = new Department("Test");
        Department saved = repository.save(department);
        assertNotNull(saved.getId());
        assertEquals("Test", saved.getName());
    }
}