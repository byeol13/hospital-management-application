package com.hospitalSB.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="clinicalrecord")

public class Clinicalrecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clinicaldata;

    private Long patientid;
}

