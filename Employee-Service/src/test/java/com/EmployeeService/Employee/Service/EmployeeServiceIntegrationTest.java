package com.EmployeeService.Employee.Service;

import com.EmployeeService.Employee.Service.controller.DepartmentClient;
import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.dto.Department;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import com.EmployeeService.Employee.Service.services.EmployeeService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {
    "spring.cloud.openfeign.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
public class EmployeeServiceIntegrationTest {

    @Configuration
    static class TestConfig {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentClient departmentClient;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        Employee employee = new Employee("John Doe", 1L);
        employeeRepository.save(employee);
        when(departmentClient.getDepartment(1L)).thenReturn((new Department(1L, "Engineering")));
    }

    @Test
    void getEmployeeWithDepartment_callsDepartmentService() {
        CombinedResponse response = employeeService.getEmployeeWithDepartment(1L);
        assertNotNull(response);
        assertEquals("John Doe", response.getEmployee().getName());
        assertEquals("Engineering", response.getDepartment().getName());
    }
}