package com.EmployeeService.Employee.Service.services;

import com.EmployeeService.Employee.Service.controller.DepartmentClient;
import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.dto.Department;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository repo;
    private final DepartmentClient deptClient;
    private final Timer employeeFetchTimer;

    public EmployeeService(EmployeeRepository repo, DepartmentClient deptClient, MeterRegistry meterRegistry) {
        this.repo = repo;
        this.deptClient = deptClient;
        this.employeeFetchTimer = Timer.builder("employee.fetch.time")
                .description("Time to fetch employee with department")
                .tags("service", "employee-service")
                .register(meterRegistry);
    }

    public Employee save(Employee employee) {
        logger.info("Saving employee with ID: {}", employee.getId());
        return repo.save(employee);
    }

    @CircuitBreaker(name = "deptServiceCircuit", fallbackMethod = "departmentFallback")
    public CombinedResponse getEmployeeWithDepartment(Long id) {
        return employeeFetchTimer.record(() -> {
            logger.info("Fetching employee with ID: {}", id);

            Employee employee = repo.findById(id)
                    .orElseThrow(() -> {
                        logger.error("Employee not found for ID: {}", id);
                        return new NoSuchElementException("Employee not found: " + id);
                    });

            Department response = deptClient.getDepartment(employee.getDepartmentId());
            Department department;
            if (response != null) {
                department = response;
            } else {
                logger.warn("Department not found for ID: {}", employee.getDepartmentId());
                department = new Department(-1L, "UNKNOWN (Department Not Found)");
            }
            return new CombinedResponse(employee, department);
        });
    }

    public CombinedResponse departmentFallback(Long id, Throwable t) {
        logger.warn("Department service fallback triggered for employee ID: {}. Error: {}", id, t.getMessage());

        Optional<Employee> employeeOpt = repo.findById(id);
        Department fallbackDept = new Department(-1L, "UNKNOWN (Service Unavailable)");
        if (employeeOpt.isEmpty()) {
            logger.error("Employee not found in fallback for ID: {}", id);
            return new CombinedResponse(null, fallbackDept);
        }
        return new CombinedResponse(employeeOpt.get(), fallbackDept);
    }
}