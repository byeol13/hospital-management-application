package com.hospitalSB.Repository;


import com.hospitalSB.Entity.Clinicalrecord;
import com.hospitalSB.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicalrecordRepository extends JpaRepository<Clinicalrecord,Long> {


    Optional<Clinicalrecord> findByClinicaldata(String clinicaldata);
    Optional<List<Clinicalrecord>> findByPatientid(Long patientid);
}



