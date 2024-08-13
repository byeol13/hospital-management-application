package com.hospitalSB.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="department")

public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;
}

