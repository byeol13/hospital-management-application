package com.hospitalSB.Service;


import com.hospitalSB.Entity.AdmissionState;
import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Entity.Department;
import com.hospitalSB.Repository.AdmissionStateRepository;
import com.hospitalSB.Repository.ClinicalrecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AdmissionStateService {

    @Autowired
    private AdmissionStateRepository admissionStateRepository;

    public AdmissionState findByPatientIdAndDischargeIsNull(Long patientid) {
        try {
            return admissionStateRepository.findByPatientIdAndDischargeIsNull(patientid).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving admission state. Please try again later.", e);
        }
    }

    @Transactional
    public void updateReasonById(Long id, String reason) {
        try {
            admissionStateRepository.updateReasonById(id, reason);
        } catch (Exception e) {
            throw new RuntimeException("Error updating reason for admission state. Please try again later.", e);
        }
    }

    @Transactional
    public void admitPatient(String cause, Long patientid, Long departmentid) {
        try {
            admissionStateRepository.admitPatient(cause, patientid, departmentid);
        } catch (Exception e) {
            throw new RuntimeException("Error admitting patient. Please try again later.", e);
        }
    }

    public AdmissionState save(AdmissionState admissionState1) {
        try {
            return admissionStateRepository.save(admissionState1);
        } catch (Exception e) {
            throw new RuntimeException("Error saving admission state. Please try again later.", e);
        }
    }

    public List<AdmissionState> getAdmissionByPatientid(Long patientid) {
        try {
            Optional<List<AdmissionState>> admissionStatesOpt = admissionStateRepository.findAdmissionByPatientid(patientid);
            return admissionStatesOpt.orElse(Collections.emptyList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving admission states by patient ID. Please try again later.", e);
        }
    }
}


