package com.EmployeeService.Employee.Service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.EmployeeService.Employee.Service.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}