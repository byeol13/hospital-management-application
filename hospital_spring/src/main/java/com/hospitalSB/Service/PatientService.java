package com.hospitalSB.Service;


import com.hospitalSB.Entity.Department;
import com.hospitalSB.Entity.Patient;
import com.hospitalSB.Repository.AdmissionStateRepository;
import com.hospitalSB.Repository.DepartmentRepository;
import com.hospitalSB.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    @Autowired
    private AdmissionStateRepository admissionStateRepository;

    public Patient createPatient(Patient patient) {
        try {
            return patientRepository.save(patient);
        } catch (Exception e) {
            throw new RuntimeException("Error creating patient. Please try again later.", e);
        }
    }

    public Patient getPatientById(Long id) {
        try {
            return patientRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving patient. Please try again later.", e);
        }
    }

    public Patient updatePatient(Patient patient1) {
        try {
            return patientRepository.save(patient1);
        } catch (Exception e) {
            throw new RuntimeException("Error updating patient. Please try again later.", e);
        }
    }

    public List<Patient> getAllPatients() {
        try {
            return patientRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving patients. Please try again later.", e);
        }
    }

    public void deletePatient(Patient patient) {
        try {
            boolean hasActiveRecords = admissionStateRepository.hasActiveRecords(patient.getId());
            if (hasActiveRecords) {
                throw new RuntimeException("Cannot delete patient with opened records");
            }
            patientRepository.delete(patient);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting patient. Please try again later.", e);
        }
    }
}


