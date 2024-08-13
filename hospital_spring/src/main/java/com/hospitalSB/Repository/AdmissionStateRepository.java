package com.hospitalSB.Repository;


import com.hospitalSB.Entity.AdmissionState;
import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Entity.Department;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionStateRepository extends JpaRepository<AdmissionState, Long> {
    @Query("SELECT a FROM AdmissionState a WHERE a.patientid = :patientId AND a.discharge=false")
    Optional<AdmissionState> findByPatientIdAndDischargeIsNull(@Param("patientId") Long patientId);

    @Transactional
    @Modifying
    @Query("UPDATE AdmissionState a SET a.discharge = true, a.exitingdate = current_timestamp(), a.reason = :reason WHERE a.id = :id")
    void updateReasonById(@Param("id") Long id, @Param("reason") String reason);

    @Transactional
    @Modifying
    @Query("INSERT INTO AdmissionState (cause, patientid, departmentid) " +
            "VALUES (:cause,:patientid,:departmentid)")
    void admitPatient(@Param("cause") String cause,
                      @Param("patientid") Long patientid,
                      @Param("departmentid") Long departmentid);


    @Query("SELECT a FROM AdmissionState a WHERE a.patientid = :patientid AND a.discharge=true")

    Optional<List<AdmissionState>> findAdmissionByPatientid(@Param("patientid") Long patientid);

    @Query("SELECT COUNT(a) > 0 FROM AdmissionState a WHERE a.departmentid = :departmentid AND a.discharge = false")
    boolean hasActivePatients(@Param("departmentid") Long departmentid);

    @Query("SELECT COUNT(a) > 0 FROM AdmissionState a WHERE a.patientid = :patientid AND a.discharge = false")
    boolean hasActiveRecords(@Param("patientid") Long patientid);


}








