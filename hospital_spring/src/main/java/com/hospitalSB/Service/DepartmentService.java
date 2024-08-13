package com.hospitalSB.Service;


import com.hospitalSB.Entity.Department;
import com.hospitalSB.Repository.AdmissionStateRepository;
import com.hospitalSB.Repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AdmissionStateRepository admissionStateRepository;

    public Department createDepartment(Department department) {
        try {
            return departmentRepository.save(department);
        } catch (Exception e) {
            throw new RuntimeException("Error creating department. Please try again later.", e);
        }
    }

    public Department getDepartmentByCode(String code) {
        try {
            return departmentRepository.findByCode(code).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving department by code. Please try again later.", e);
        }
    }

    public Department updateDepartment(Department department1) {
        try {
            return departmentRepository.save(department1);
        } catch (Exception e) {
            throw new RuntimeException("Error updating department. Please try again later.", e);
        }
    }

    public List<Department> getAllDepartments() {
        try {
            return departmentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all departments. Please try again later.", e);
        }
    }

    public void deleteDepartment(Department department) {
        try {
            boolean hasActivePatients = admissionStateRepository.hasActivePatients(department.getId());
            if (hasActivePatients) {
                throw new RuntimeException("Cannot delete department with active patients");
            }
            departmentRepository.delete(department);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting department. Please try again later.", e);
        }
    }


}


