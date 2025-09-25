package com.departmentService.department.service;

import com.departmentService.department.service.model.Department;
import com.departmentService.department.service.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DepartmentControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DepartmentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testSaveDepartment() {
        Department department = new Department("Engineering");
        ResponseEntity<Department> response = restTemplate.postForEntity("/departments", department, Department.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Engineering", response.getBody().getName());
    }

    @Test
    void testFindById() {
        Department department = new Department("HR");
        department = repository.save(department);

        ResponseEntity<Department> response = restTemplate.getForEntity("/departments/" + department.getId(), Department.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("HR", response.getBody().getName());
    }

    @Test
    void testFindByIdNotFound() {
        ResponseEntity<Map> response = restTemplate.getForEntity("/departments/999", Map.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Department with id 999 not found", response.getBody().get("error"));
    }
}