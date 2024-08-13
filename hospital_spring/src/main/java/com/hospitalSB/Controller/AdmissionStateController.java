package com.hospitalSB.Controller;

import com.hospitalSB.Entity.AdmissionState;
import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Entity.Department;
import com.hospitalSB.Entity.Patient;
import com.hospitalSB.Service.AdmissionStateService;
import com.hospitalSB.Service.ClinicalrecordService;
import com.hospitalSB.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdmissionStateController {

    @Autowired
    private AdmissionStateService admissionStateService;

    @GetMapping("/admissionstate/{patientid}")
    public ResponseEntity<?> findByPatientIdAndExitingDateIsNull(@PathVariable Long patientid) {
        try {
            AdmissionState admissionState = admissionStateService.findByPatientIdAndDischargeIsNull(patientid);
            if (admissionState == null) {
                System.out.println("No rows found for patient ID: " + patientid); // Log message
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(admissionState);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving admission state. Please try again later.");
        }
    }

    @PutMapping("/admissionstateUpd/{patientid}")
    public ResponseEntity<?> updateReason(@RequestBody AdmissionState admissionState, @PathVariable Long patientid) {
        try {
            AdmissionState admissionState1 = admissionStateService.findByPatientIdAndDischargeIsNull(patientid);
            if (admissionState1 == null) {
                return ResponseEntity.notFound().build();
            }
            admissionState1.setReason(admissionState.getReason());
            admissionStateService.updateReasonById(admissionState1.getId(), admissionState.getReason());
            return ResponseEntity.ok(admissionState1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating admission state reason. Please try again later.");
        }
    }

    @PutMapping("/admissionstateAdmit/{patientid}")
    public ResponseEntity<?> admitPatient(@RequestBody AdmissionState admissionState, @PathVariable Long patientid) {
        try {
            AdmissionState admissionState1 = admissionStateService.findByPatientIdAndDischargeIsNull(patientid);
            if (admissionState1 == null) {
                AdmissionState newAdmissionState = new AdmissionState();
                newAdmissionState.setEnteringdate(LocalDateTime.now()); // Set entering date
                newAdmissionState.setCause(admissionState.getCause());
                newAdmissionState.setPatientid(admissionState.getPatientid());
                newAdmissionState.setDepartmentid(admissionState.getDepartmentid());
                newAdmissionState.setDischarge(false); // Set discharge status to false
                AdmissionState savedAdmissionState = admissionStateService.save(newAdmissionState);
                return ResponseEntity.ok(savedAdmissionState);
            } else {
                System.out.println("Patient has not been discharged yet");
                return ResponseEntity.badRequest().body("Patient has not been discharged yet.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error admitting patient. Please try again later.");
        }
    }

    @GetMapping("/admissionstateByPId/{patientid}")
    public ResponseEntity<?> getAdmissionByPatientid(@PathVariable Long patientid) {
        try {
            List<AdmissionState> admissionStates = admissionStateService.getAdmissionByPatientid(patientid);
            if (admissionStates.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(admissionStates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving admission states by patient ID. Please try again later.");
        }
    }


}
