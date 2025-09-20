package com.EmployeeService.Employee.Service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // this is the foreign key reference to Department
    private Long departmentId;

    
    
    public Employee() {
		super();
	}

	public Employee(String name, Long departmentId) {
		this.name = name;
		this.departmentId = departmentId;
	}

	// --- getters and setters ---
    public Long getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
