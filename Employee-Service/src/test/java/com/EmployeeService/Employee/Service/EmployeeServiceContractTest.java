package com.EmployeeService.Employee.Service;

import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import com.EmployeeService.Employee.Service.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureStubRunner(
    ids = "com.departmentService:department-service:+:stubs",
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@ActiveProfiles("test")
public class EmployeeServiceContractTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
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