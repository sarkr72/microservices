package com.DepartmentService.Department.Service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.DepartmentService.Department.Service.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
