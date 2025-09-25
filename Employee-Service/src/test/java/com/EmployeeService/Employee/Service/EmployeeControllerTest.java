package com.EmployeeService.Employee.Service;

import com.EmployeeService.Employee.Service.controller.EmployeeController;
import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.dto.Department;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.services.EmployeeService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.yml", properties = {
    "spring.cloud.openfeign.enabled=false",
    "spring.main.allow-bean-definition-overriding=true"
})
public class EmployeeControllerTest {

    @Configuration
    static class TestConfig {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeWithDepartment_shouldReturn200() throws Exception {
        CombinedResponse response = new CombinedResponse(
            new Employee("John Doe", 1L),
            new Department(1L, "Engineering")
        );
        when(employeeService.getEmployeeWithDepartment(1L)).thenReturn(response);

        mockMvc.perform(get("/employees/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.employee.name").value("John Doe"))
            .andExpect(jsonPath("$.department.name").value("Engineering"));
    }
}