package com.EmployeeService.Employee.Service;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import com.EmployeeService.Employee.Service.services.EmployeeService;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureWireMock(port = 9999)
public class EmployeeServiceIntegrationTest {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        Employee employee = new Employee( "John Doe", 1L);
        employeeRepository.save(employee);
    }

    @Test
    void getEmployeeWithDepartment_callsDepartmentService() {
        stubFor(get(urlEqualTo("/departments/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"id\":1,\"name\":\"Engineering\"}")));

        CombinedResponse response = employeeService.getEmployeeWithDepartment(1L);
        assertNotNull(response);
        assertEquals("John Doe", response.getEmployee().getName());
        assertEquals("Engineering", response.getDepartment().getName());
    }
}