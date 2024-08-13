package com.hospitalSB.Controller;

import com.hospitalSB.Entity.Department;
import com.hospitalSB.Entity.Patient;
import com.hospitalSB.Service.DepartmentService;
import com.hospitalSB.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/patient")
    public ResponseEntity<?> createPatient(@RequestBody Patient patient) {
        try {
            Patient createdPatient = patientService.createPatient(patient);
            return ResponseEntity.ok(createdPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating patient. Please try again later.");
        }
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving patient. Please try again later.");
        }
    }

    @PutMapping("/patient/{id}")
    public ResponseEntity<?> updatePatient(@RequestBody Patient patient, @PathVariable Long id) {
        try {
            Patient patient1 = patientService.getPatientById(id);
            if (patient1 == null) {
                return ResponseEntity.notFound().build();
            }
            patient1.setName(patient.getName());
            patient1.setLastname(patient.getLastname());
            patient1.setBirthdate(patient.getBirthdate());
            Patient updatedPatient = patientService.updatePatient(patient1);
            return ResponseEntity.ok(updatedPatient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating patient. Please try again later.");
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            return ResponseEntity.ok(patients);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving patients. Please try again later.");
        }
    }

    @DeleteMapping("/patientDlt/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id) {
        try {
            Patient patient = patientService.getPatientById(id);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            patientService.deletePatient(patient);
            return ResponseEntity.ok(patient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting patient. Please try again later.");
        }
    }


}

