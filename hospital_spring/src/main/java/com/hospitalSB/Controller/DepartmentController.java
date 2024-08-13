package com.hospitalSB.Controller;

import com.hospitalSB.Entity.Department;
import com.hospitalSB.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/department")
    public ResponseEntity<?> createDepartment(@RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.ok(createdDepartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating department. Please try again later.");
        }
    }

    @GetMapping("/department/{code}")
    public ResponseEntity<?> getDepartmentByCode(@PathVariable String code) {
        try {
            Department department = departmentService.getDepartmentByCode(code);
            if (department == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving department. Please try again later.");
        }
    }

    @PutMapping("/department/{code}")
    public ResponseEntity<?> updateDepartment(@RequestBody Department department, @PathVariable String code) {
        try {
            Department department1 = departmentService.getDepartmentByCode(code);
            if (department1 == null) {
                return ResponseEntity.notFound().build();
            }
            department1.setCode(department.getCode());
            department1.setName(department.getName());
            Department updatedDepartment = departmentService.updateDepartment(department1);
            return ResponseEntity.ok(updatedDepartment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating department. Please try again later.");
        }
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments() {
        try {
            List<Department> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving departments. Please try again later.");
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteDepartment(@PathVariable String code) {
        try {
            Department department = departmentService.getDepartmentByCode(code);
            if (department == null) {
                return ResponseEntity.notFound().build();
            }
            departmentService.deleteDepartment(department);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting department. Please try again later.");
        }
    }
}
