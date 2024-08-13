package com.hospitalSB.Service;


import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Repository.ClinicalrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicalrecordService {

    @Autowired
    private ClinicalrecordRepository clinicalrecordRepository;


    public Clinicalrecord createClinicalrecord(Clinicalrecord clinicalrecord) {
        try {
            return clinicalrecordRepository.save(clinicalrecord);
        } catch (Exception e) {
            throw new RuntimeException("Error creating clinical record. Please try again later.", e);
        }
    }

    public Clinicalrecord getClinicalrecordByClinicaldata(String clinicaldata) {
        try {
            return clinicalrecordRepository.findByClinicaldata(clinicaldata).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving clinical record by clinical data. Please try again later.", e);
        }
    }

    public Clinicalrecord updateClinicalrecord(Clinicalrecord clinicalrecord1) {
        try {
            return clinicalrecordRepository.save(clinicalrecord1);
        } catch (Exception e) {
            throw new RuntimeException("Error updating clinical record. Please try again later.", e);
        }
    }

    public List<Clinicalrecord> getAllClinicalrecords() {
        try {
            return clinicalrecordRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all clinical records. Please try again later.", e);
        }
    }

    public void deleteClinicalrecord(Clinicalrecord clinicalrecord) {
        try {
            clinicalrecordRepository.delete(clinicalrecord);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting clinical record. Please try again later.", e);
        }
    }

    public List<Clinicalrecord> getClinicalrecordByPatientid(Long patientid) {
        try {
            Optional<List<Clinicalrecord>> clinicalRecordsOptional = clinicalrecordRepository.findByPatientid(patientid);
            return clinicalRecordsOptional.orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving clinical records by patient ID. Please try again later.", e);
        }
    }

}


