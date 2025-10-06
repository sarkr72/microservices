package com.EmployeeService.Employee.Service;

import com.EmployeeService.Employee.Service.controller.EmployeeController;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {
    "spring.cloud.openfeign.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
public abstract class BaseTestClass {

    @Configuration
    static class TestConfig {
        @Bean
        io.micrometer.core.instrument.MeterRegistry meterRegistry() {
            return new io.micrometer.core.instrument.simple.SimpleMeterRegistry();
        }
    }

    @Autowired
    protected EmployeeController employeeController;

    @Autowired
    protected EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
        Employee employee = new Employee("John Doe", 1L);
        employeeRepository.save(employee);
        RestAssuredMockMvc.standaloneSetup(employeeController);
    }
}
