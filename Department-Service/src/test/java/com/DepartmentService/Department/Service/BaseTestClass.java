package com.departmentService.department.service;

import com.departmentService.department.service.controller.DepartmentController;
import com.departmentService.department.service.model.Department;
import com.departmentService.department.service.repository.DepartmentRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class BaseTestClass {

    @Autowired
    protected DepartmentController departmentController;

    @Autowired
    protected DepartmentRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        Department department = new Department("Engineering");
        repository.save(department);
        RestAssuredMockMvc.standaloneSetup(departmentController);
    }
}