package com.EmployeeService.Employee.Service.dto;

import com.EmployeeService.Employee.Service.model.Employee;

public class CombinedResponse {

    private Employee employee;
    private Department department;

    public CombinedResponse(Employee employee, Department department) {
        this.employee = employee;
        this.department = department;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}