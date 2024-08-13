package com.hospitalSB.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="admissionstate")

public class AdmissionState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime enteringdate;
    private LocalDateTime exitingdate;
    private String cause;
    private String reason;
    private boolean discharge;
    private Long patientid;
    private Long departmentid;



}

