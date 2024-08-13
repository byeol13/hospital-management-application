package com.hospitalSB.Repository;


import com.hospitalSB.Entity.Department;
import com.hospitalSB.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {


    Optional<Patient> findById(Long id);
}




