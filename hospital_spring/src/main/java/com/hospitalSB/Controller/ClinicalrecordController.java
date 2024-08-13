package com.hospitalSB.Controller;

import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Entity.Department;
import com.hospitalSB.Service.ClinicalrecordService;
import com.hospitalSB.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClinicalrecordController {

    @Autowired
    private ClinicalrecordService clinicalrecordService;

    @PostMapping("/clinicalrecord")
    public ResponseEntity<?> createClinicalrecord(@RequestBody Clinicalrecord clinicalrecord) {
        try {
            Clinicalrecord createdClinicalrecord = clinicalrecordService.createClinicalrecord(clinicalrecord);
            return ResponseEntity.ok(createdClinicalrecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating clinical record. Please try again later.");
        }
    }

    @GetMapping("/clinicalrecord/{clinicaldata}")
    public ResponseEntity<?> getClinicalrecordByClinicaldata(@PathVariable String clinicaldata) {
        try {
            Clinicalrecord clinicalrecord = clinicalrecordService.getClinicalrecordByClinicaldata(clinicaldata);
            if (clinicalrecord == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(clinicalrecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving clinical record. Please try again later.");
        }
    }

    @GetMapping("/clinicalrecordById/{patientid}")
    public ResponseEntity<?> getClinicalrecordByPatientid(@PathVariable Long patientid) {
        try {
            List<Clinicalrecord> clinicalrecords = clinicalrecordService.getClinicalrecordByPatientid(patientid);
            if (clinicalrecords.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(clinicalrecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving clinical records. Please try again later.");
        }
    }

    @PutMapping("/clinicalrecord/{clinicaldata}")
    public ResponseEntity<?> updateClinicalrecord(@RequestBody Clinicalrecord clinicalrecord, @PathVariable String clinicaldata) {
        try {
            Clinicalrecord clinicalrecord1 = clinicalrecordService.getClinicalrecordByClinicaldata(clinicaldata);
            if (clinicalrecord1 == null) {
                return ResponseEntity.notFound().build();
            }
            clinicalrecord1.setClinicaldata(clinicalrecord.getClinicaldata());
            Clinicalrecord updatedClinicalrecord = clinicalrecordService.updateClinicalrecord(clinicalrecord1);
            return ResponseEntity.ok(updatedClinicalrecord);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating clinical record. Please try again later.");
        }
    }

    @GetMapping("/clinicalrecords")
    public ResponseEntity<?> getAllClinicalrecords() {
        try {
            List<Clinicalrecord> clinicalrecords = clinicalrecordService.getAllClinicalrecords();
            return ResponseEntity.ok(clinicalrecords);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving clinical records. Please try again later.");
        }
    }

    @DeleteMapping("/clinicalrecordDl/{clinicaldata}")
    public ResponseEntity<?> deleteClinicalrecord(@PathVariable String clinicaldata) {
        try {
            Clinicalrecord clinicalrecord = clinicalrecordService.getClinicalrecordByClinicaldata(clinicaldata);
            if (clinicalrecord == null) {
                return ResponseEntity.notFound().build();
            }
            clinicalrecordService.deleteClinicalrecord(clinicalrecord);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting clinical record. Please try again later.");
        }
    }
}
