package com.hospital_android_project.app.model;

import java.io.Serializable;

public class Clinicalrecord implements Serializable {
    private Long id;
    private String clinicaldata;
    private Long patientid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClinicaldata() {
        return clinicaldata;
    }

    public void setClinicaldata(String clinicaldata) {
        this.clinicaldata = clinicaldata;
    }

    public Long getPatientid() {
        return patientid;
    }

    public void setPatientid(Long patientid) {
        this.patientid = patientid;
    }

    @Override
    public String toString() {
        return "ClinicalRecord{" +
                "id=" + id +
                ", clinicaldata='" + clinicaldata + '\'' +
                ", patientid=" + patientid +
                '}';
    }
}
