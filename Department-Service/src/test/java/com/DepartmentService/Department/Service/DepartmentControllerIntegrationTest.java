package com.DepartmentService.Department.Service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.DepartmentService.Department.Service.model.Department;
import com.DepartmentService.Department.Service.repositories.DepartmentRepository;

@SpringBootTest(properties = "spring.profiles.active=test")
public class DepartmentControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Department department = new Department("Engineering");
        departmentRepository.save(department);
    }

    @Test
    void getDepartment_shouldReturn200() throws Exception {
        mockMvc.perform(get("/departments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Engineering"));
    }
}