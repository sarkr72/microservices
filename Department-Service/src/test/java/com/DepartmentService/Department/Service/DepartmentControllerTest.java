package com.DepartmentService.Department.Service;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.DepartmentService.Department.Service.controller.DepartmentController;
import com.DepartmentService.Department.Service.model.Department;
import com.DepartmentService.Department.Service.repositories.DepartmentRepository;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.DepartmentService.Department.Service.controller.DepartmentController;
//
//@WebMvcTest(DepartmentController.class)
//public class DepartmentControllerTest {
//	
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void getDepartment_shouldReturn200() throws Exception {
//        mockMvc.perform(get("/departments/1"))
//            .andExpect(status().isOk());
//    }
//}
//
//


@WebMvcTest(DepartmentController.class)
@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DepartmentRepository departmentRepository;

    @Test
    void getDepartment_shouldReturn200() throws Exception {
        Department department = new Department("Engineering");
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        mockMvc.perform(get("/departments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Engineering"));
    }

    @Test
    void getDepartment_notFound_shouldReturn404() throws Exception {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/departments/1"))
            .andExpect(status().isNotFound());
    }
}