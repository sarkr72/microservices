package com.EmployeeService.Employee.Service.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.EmployeeService.Employee.Service.controller.DepartmentClient;
import com.EmployeeService.Employee.Service.dto.CombinedResponse;
import com.EmployeeService.Employee.Service.dto.Department;
import com.EmployeeService.Employee.Service.model.Employee;
import com.EmployeeService.Employee.Service.repositories.EmployeeRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;


@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    
    private final EmployeeRepository repo;
    private final DepartmentClient deptClient;
    private final Timer employeeFetchTimer;
    private final Tracer tracer;

    /**
     * Constructor with dependency injection for repository, Feign client, metrics, and tracing.
     *
     * @param repo         Employee repository for database operations
     * @param deptClient   Feign client for department service
     * @param meterRegistry Micrometer registry for Prometheus metrics
     * @param tracer       OpenTelemetry tracer for distributed tracing
     */
    public EmployeeService(EmployeeRepository repo, DepartmentClient deptClient, 
                          MeterRegistry meterRegistry, Tracer tracer) {
        this.repo = repo;
        this.deptClient = deptClient;
        this.tracer = tracer;
        this.employeeFetchTimer = Timer.builder("employee.fetch.time")
                .description("Time to fetch employee with department")
                .tags("service", "employee-service")
                .register(meterRegistry);
    }

    /**
     * Saves an employee to the database.
     *
     * @param employee The employee to save
     * @return The saved employee
     */
    public Employee save(Employee employee) {
        Span span = tracer.spanBuilder("employee.save").startSpan();
        try (var scope = span.makeCurrent()) {
            span.setAttribute("employee.id", employee.getId() != null ? employee.getId() : -1L);
            logger.info("Saving employee with ID: {}", employee.getId());
            return repo.save(employee);
        } finally {
            span.end();
        }
    }

    /**
     * Fetches an employee by ID along with their department details.
     * Uses Resilience4j circuit breaker for fault tolerance when calling department service.
     *
     * @param id Employee ID
     * @return CombinedResponse containing employee and department
     * @throws NoSuchElementException if employee not found
     */
    @CircuitBreaker(name = "deptServiceCircuit", fallbackMethod = "departmentFallback")
    public CombinedResponse getEmployeeWithDepartment(Long id) {
        return employeeFetchTimer.record(() -> {
            Span span = tracer.spanBuilder("employee.getWithDepartment").startSpan();
            try (var scope = span.makeCurrent()) {
                span.setAttribute("employee.id", id);
                logger.info("Fetching employee with ID: {}", id);

                Employee employee = repo.findById(id)
                        .orElseThrow(() -> {
                            span.setAttribute("error", "Employee not found");
                            logger.error("Employee not found for ID: {}", id);
                            return new NoSuchElementException("Employee not found: " + id);
                        });

                span.setAttribute("department.id", employee.getDepartmentId());
                Department department = deptClient.getDepartment(employee.getDepartmentId());
                return new CombinedResponse(employee, department);
            } finally {
                span.end();
            }
        });
    }

    /**
     * Fallback method for getEmployeeWithDepartment when department service fails.
     *
     * @param id Employee ID
     * @param t  Throwable causing the failure
     * @return CombinedResponse with employee (if found) and a fallback department
     */
    public CombinedResponse departmentFallback(Long id, Throwable t) {
        Span span = tracer.spanBuilder("employee.departmentFallback").startSpan();
        try (var scope = span.makeCurrent()) {
            span.setAttribute("employee.id", id);
            span.setAttribute("error.type", t.getClass().getSimpleName());
            span.setAttribute("error.message", t.getMessage());
            logger.warn("Department service fallback triggered for employee ID: {}. Error: {}", id, t.getMessage());

            Optional<Employee> employeeOpt = repo.findById(id);
            Department fallbackDept = new Department(-1L, "UNKNOWN (Service Unavailable)");
            if (employeeOpt.isEmpty()) {
                logger.error("Employee not found in fallback for ID: {}", id);
                span.setAttribute("employee.found", false);
                return new CombinedResponse(null, fallbackDept);
            }
            return new CombinedResponse(employeeOpt.get(), fallbackDept);
        } finally {
            span.end();
        }
    }
}

//@Service
//public class EmployeeService {
//	private final EmployeeRepository repo;
//	private final DepartmentClient deptClient;
//
//	private final Timer employeeFetchTimer;
//	
//	public EmployeeService(EmployeeRepository repo, DepartmentClient deptClient) {
//		this.repo = repo;
//		this.deptClient = deptClient;
//	}
//
//	public Employee save(Employee e) {
//		return repo.save(e);
//	}
//
//	// Use Resilience4j's CircuitBreaker via Spring Cloud decorator
//	@CircuitBreaker(name = "deptServiceCircuit", fallbackMethod = "departmentFallback")
//	public CombinedResponse getEmployeeWithDepartment(Long id) {
//		Employee emp = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Employee not found"));
//		Department dept = deptClient.getDepartment(emp.getDepartmentId()); // may throw
//		return new CombinedResponse(emp, dept);
//	}
//
//	public CombinedResponse departmentFallback(Long id, Throwable t) {
//		// return employee with placeholder department, or cached value, or partial
//		// response
//		Employee emp = repo.findById(id).orElse(null);
//		Department fallbackDept = new Department(-1L, "UNKNOWN (fallback)");
//		return new CombinedResponse(emp, fallbackDept);
//	}
//
//
//}
