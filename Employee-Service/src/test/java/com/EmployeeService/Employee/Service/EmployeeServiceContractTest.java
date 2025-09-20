package com.EmployeeService.Employee.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import com.EmployeeService.Employee.Service.services.EmployeeService;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureStubRunner(
    ids = "com.example:department-service:+:stubs:9999",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class EmployeeServiceContractTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        Employee employee = new Employee("John Doe", 1L);
        employeeRepository.save(employee);
    }

    @Test
    void getEmployeeWithDepartment_verifiesContract() {
        CombinedResponse response = employeeService.getEmployeeWithDepartment(1L);
        assertEquals("John Doe", response.getEmployee().getName());
        assertEquals("Engineering", response.getDepartment().getName());
    }
}